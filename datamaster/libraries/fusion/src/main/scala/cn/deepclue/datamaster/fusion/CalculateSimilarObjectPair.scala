package cn.deepclue.datamaster.fusion

import cn.deepclue.datamaster.streamer.io.SchemaConverter
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by lah on 2017/5/12.
  */
object CalculateSimilarObjectPair {
  def generateEmptyRange(min: Double, max: Double): ArrayBuffer[String] = {
    val buffer = new ArrayBuffer[String]()
    val rangeArray = range(min, max, 10)
    for (i <- 0 until rangeArray.length - 1) {
      buffer += rangeArray(i) + ":" + rangeArray(i + 1) + ":" + 0
    }
    buffer
  }

  def generateOneRange(min: Double, max: Double, weight: Double, count: Long): ArrayBuffer[String] = {
    val buffer = new ArrayBuffer[String]()
    val rangeArray = range(min, max, 10)
    val len = rangeArray.length - 1
    for (i <- 0 until len) {
      val num = if ((weight >= rangeArray(i) && weight < rangeArray(i + 1)) || (rangeArray(i + 1) == weight && i == len - 1)) count else 0
      buffer += rangeArray(i) + ":" + rangeArray(i + 1) + ":" + num
    }
    buffer
  }

  def range(min: Double, max: Double, steps: Int): IndexedSeq[Double] = {
    if (min == max) {
      IndexedSeq(0, 0, 0, 0, 0, 0, 0, 0, 0, min, max)
    } else {
      val span = max - min
      Range.Int(0, steps, 1).map(s => min + (s * span) / steps) :+ max
    }
  }

  def weightSum(fieldWeight: mutable.HashMap[Int, Double]): Double = {
    var maxThreshold: Double = 0.0d
    for (weight <- fieldWeight.values) {
      maxThreshold += weight
    }
    maxThreshold
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf(true).setAppName("CalcSimilarObjectPair").set("spark.hadoop.validateOutputSpecs", "false")
    val sc = new SparkContext(conf)
    val ss = new SQLContext(sc)

    //获取配置信息
    val config = CommonUtils.readParam()

    //存储内部fusion keys
    val fusionKeysPath = FileUtils.fusionKeysPath(config.address, config.fsID)
    sc.parallelize(config.fusionKeys).coalesce(1).saveAsTextFile(fusionKeysPath)

    //码址属性序列
    val propertyTypeNames: ArrayBuffer[String] = config.fields
    val globalPropertyTypes = sc.broadcast(propertyTypeNames)

    //各码址权重map
    val propertyWeightMap: mutable.HashMap[Int, Double] = config.fieldWeights
    val broadcastPropertyWeightMap = sc.broadcast(propertyWeightMap)

    //对象相似阈值
    val threshold = config.threshold
    val broadcastThreshold = sc.broadcast(threshold)

    //特殊的单码址
    val specialField = config.specialField

    //权重最大value
    val maxThreshold: Double = weightSum(propertyWeightMap)
    val broadcastMaxThreshold = sc.broadcast(maxThreshold)

    //表数据地址列表
    val tablePaths: ListBuffer[String] = FileUtils.readTablePaths(sc, config.address, FileUtils.tablePath(config.fsID))

    var generalPropertyIndexValue_RowIndex_RDD: RDD[((Int, Long), Long)] = sc.emptyRDD[((Int, Long), Long)]
    var uniqueValue_RowIndex_RDD: RDD[(Long, Long)] = sc.emptyRDD[(Long, Long)]

    var allRowRDD: RDD[(Int, ArrayBuffer[(Int, Long)], String)] = sc.emptyRDD[(Int, ArrayBuffer[(Int, Long)], String)]

    /**
      * 从表读取行信息
      */
    for (i <- tablePaths.indices) {
      val path = tablePaths(i)

      //读取avro文件，生成DataFrame
      val df = ss.read.format("com.databricks.spark.avro").load(path)

      //注册临时表
      val tmpTabName = "tmp" + i
      df.registerTempTable(tmpTabName)

      val encodedSpecialField = "_" + SchemaConverter.base32Encode(specialField)
      if (CommonUtils.contains(df.columns, encodedSpecialField)) {
        val rowRDD = ss.sql("select uuid," + encodedSpecialField + CommonUtils.makeSelectField(df.columns, propertyTypeNames) + " from " + tmpTabName)

        //广播columns信息
        val col_b = sc.broadcast(rowRDD.columns)

        val perID_Row_RDD: RDD[(Int, ArrayBuffer[(Int, Long)], String)] = rowRDD.rdd.map(r => {
          val buffer: ArrayBuffer[(Int, Long)] = new ArrayBuffer[(Int, Long)]
          val cols = col_b.value
          val colNum = cols.length
          val pts = globalPropertyTypes.value

          val id = r(0).toString
          for (i <- 1 until colNum) {
            if (r(i) != null && !r(i).toString.isEmpty) {
              val colName = SchemaConverter.base32Decode(cols(i).substring(1))
              val colValue = CommonUtils.HashStringToLong(r(i).toString)
              val colIndex = CommonUtils.indexOf(pts, colName)

              buffer += ((colIndex, colValue))
            }
          }

          val hasSpecialField = if (buffer.length == 0) 2 else {
            if (buffer(0)._1 == -1) 0 else 1
          }

          (hasSpecialField, buffer, id)
        })

        allRowRDD = allRowRDD.union(perID_Row_RDD)
      } else {
        val rowRDD = ss.sql("select uuid" + CommonUtils.makeSelectField(df.columns, propertyTypeNames) + " from " + tmpTabName)

        //广播columns信息
        val col_b = sc.broadcast(rowRDD.columns)

        val nonePerId_Row_RDD: RDD[(Int, ArrayBuffer[(Int, Long)], String)] = rowRDD.rdd.map(r => {
          val buffer = new ArrayBuffer[(Int, Long)]

          val cols = col_b.value
          val colNum = cols.length
          val pts = globalPropertyTypes.value

          val id = r(0).toString
          for (i <- 1 until colNum) {
            if (r(i) != null && !r(i).toString.isEmpty) {
              val colName = SchemaConverter.base32Decode(cols(i).substring(1))
              val colValue = CommonUtils.HashStringToLong(r(i).toString)
              val colIndex = CommonUtils.indexOf(pts, colName)

              buffer += ((colIndex, colValue))
            }
          }

          (1, buffer, id)
        })

        allRowRDD = allRowRDD.union(nonePerId_Row_RDD)
      }
    }

    /**
      * 从其他fusion workspace读取行信息
      */
    var allSimilarPairRDD: RDD[(String, String)] = sc.emptyRDD[(String, String)]
    val fusionWSLen = if (config.fusionKeys == null) 0 else config.fusionKeys.size
    if (fusionWSLen > 0) {
      for (i <- 0 until fusionWSLen) {
        val similar_pairPath = FileUtils.similarPairPath(config.address, config.fusionKeys(i))
        val similarPairStringRDD: RDD[String] = sc.textFile(similar_pairPath)

        //相似对的逆序
        val similarPairRDD: RDD[(String, String)] = similarPairStringRDD.map(x => {
          val sp = x.stripPrefix("(").stripSuffix(")").split(",")
          (sp(1), sp(0))
        }).filter(x => x._1 != x._2)

        allSimilarPairRDD = allSimilarPairRDD.union(similarPairRDD)
        var tmpAllRowRDD: RDD[(String, (ArrayBuffer[(Int, Long)], Int))] = sc.emptyRDD[(String, (ArrayBuffer[(Int, Long)], Int))]

        //表数据地址列表
        val tablePaths: ListBuffer[String] = FileUtils.readTablePaths(sc, config.address, FileUtils.tablePath(config.fusionKeys(i)))
        for (j <- tablePaths.indices) {
          val path = tablePaths(j)

          //读取avro文件，生成DataFrame
          val df = ss.read.format("com.databricks.spark.avro").load(path)

          //注册临时表
          val tmpTabName = "tmp" + i + j
          df.registerTempTable(tmpTabName)

          val encodedSpecialField = "_" + SchemaConverter.base32Encode(specialField)
          if (CommonUtils.contains(df.columns, encodedSpecialField)) {
            val rowRDD = ss.sql("select uuid," + encodedSpecialField + CommonUtils.makeSelectField(df.columns, propertyTypeNames) + " from " + tmpTabName)

            //广播columns信息
            val col_b = sc.broadcast(rowRDD.columns)

            val perID_Row_RDD: RDD[(String, (ArrayBuffer[(Int, Long)], Int))] = rowRDD.rdd.map(r => {
              val buffer: ArrayBuffer[(Int, Long)] = new ArrayBuffer[(Int, Long)]
              val cols = col_b.value
              val colNum = cols.length
              val pts = globalPropertyTypes.value

              val id = r(0).toString
              for (i <- 1 until colNum) {
                if (r(i) != null && !r(i).toString.isEmpty) {
                  val colName = SchemaConverter.base32Decode(cols(i).substring(1))
                  val colValue = CommonUtils.HashStringToLong(r(i).toString)
                  val colIndex = CommonUtils.indexOf(pts, colName)

                  buffer += ((colIndex, colValue))
                }
              }

              (id, (buffer, 0))
            })

            tmpAllRowRDD = tmpAllRowRDD.union(perID_Row_RDD)
          } else {
            val rowRDD = ss.sql("select uuid" + CommonUtils.makeSelectField(df.columns, propertyTypeNames) + " from " + tmpTabName)

            //广播columns信息
            val col_b = sc.broadcast(rowRDD.columns)

            val nonePerId_Row_RDD: RDD[(String, (ArrayBuffer[(Int, Long)], Int))] = rowRDD.rdd.map(r => {
              val buffer = new ArrayBuffer[(Int, Long)]

              val cols = col_b.value
              val colNum = cols.length
              val pts = globalPropertyTypes.value

              val id = r(0).toString
              for (i <- 1 until colNum) {
                if (r(i) != null && !r(i).toString.isEmpty) {
                  val colName = SchemaConverter.base32Decode(cols(i).substring(1))
                  val colValue = CommonUtils.HashStringToLong(r(i).toString)
                  val colIndex = CommonUtils.indexOf(pts, colName)

                  buffer += ((colIndex, colValue))
                }
              }

              (id, (buffer, 1))
            })

            tmpAllRowRDD = tmpAllRowRDD.union(nonePerId_Row_RDD)
          }
        }

        //相似对数据需要融合到追加的对象上
        val formatAllRowRDD = tmpAllRowRDD.leftOuterJoin(similarPairRDD).map(x => {
          if (x._2._2.isEmpty) {
            (x._1, (x._2._1._2, x._2._1._1))
          } else {
            (x._2._2.get, (x._2._1._2, x._2._1._1))
          }
        }).groupByKey().map(x => {
          var set = new mutable.HashSet[(Int, Long)]()
          for (i <- x._2) {
            set ++= i._2
          }
          var buf = new ArrayBuffer[(Int, Long)]()
          buf ++= set
          buf = buf.sortWith((x, y) => x._1 < y._1)

          val hasSpecialField = if (buf.length == 0) 2 else {
            if (buf(0)._1 == -1) 0 else 1
          }
          (hasSpecialField, buf, x._1)
        })

        allRowRDD = allRowRDD.union(formatAllRowRDD)
      }
    }

    //汇集其他fusion store中的追加对象,用于统一index
    allRowRDD = allRowRDD.union(allSimilarPairRDD.map(x => {
      (2, new ArrayBuffer[(Int, Long)](), x._1)
    }))

    //生成每行信息
    //格式:( (1 or 0,List[(属性index, 属性值)],uuid), rowIndex )
    //0或者1代表是否包含特殊码址, rowIndex表示每行全局的long id
    val allRowIndexRDD = allRowRDD.zipWithIndex()

    //所有(uuid : rowIndex) Pair
    val allObjectIndexRDD = allRowIndexRDD.map(x => (x._2, x._1._3))

    val allIndexObjectRDD = allObjectIndexRDD.map(x => (x._2, x._1))
    val handledObjectPairRDD = allSimilarPairRDD.join(allIndexObjectRDD).map(x => (x._2._1, x._2._2)).join(allIndexObjectRDD).map(x => x._2)

    //包含特殊码址的行信息
    val uniqueRowIndexRDD = allRowIndexRDD.filter(x => x._1._1 == 0).cache()

    //格式[(特殊码址value : rowIndex)]
    uniqueValue_RowIndex_RDD = uniqueRowIndexRDD.map(x => (x._1._2(0)._2, x._2))

    //不包含特殊码址的行信息
    val generalRowIndexRDD = allRowIndexRDD.filter(x => x._1._1 == 1).cache()

    //格式[((属性index, value), rowIndex)]
    generalPropertyIndexValue_RowIndex_RDD = generalRowIndexRDD.flatMap(x => {
      val b1: ArrayBuffer[(Int, Long)] = x._1._2
      val b2: ArrayBuffer[((Int, Long), Long)] = new ArrayBuffer[((Int, Long), Long)]()

      for (y <- b1) {
        b2 += ((y, x._2))
      }
      b2
    })

    val generalPropertyIndexValue_IndexList_RDD: RDD[((Int, Long), Iterable[Long])] = generalPropertyIndexValue_RowIndex_RDD.groupByKey()

    ////普通多码址相似对及权重
    val generalBlendingObjPairWeightRDD: RDD[((Long, Long), Double)] = generalPropertyIndexValue_IndexList_RDD.filter(m => m._2.size > 1).flatMap(m => {
      //对象index集合
      val set:mutable.HashSet[Long] = new mutable.HashSet[Long]()
      set ++= m._2

      val buffer: ArrayBuffer[((Long, Long, Int), Double)] = new ArrayBuffer[((Long, Long, Int), Double)]()
      //过滤自身相似情况
      if (set.size > 1) {
        val weightMap = broadcastPropertyWeightMap.value
        val index = m._1._1
        val weight = weightMap.getOrElse(index, 0d)

        var seq = new ArrayBuffer[Long]
        seq.appendAll(m._2)
        seq = seq.sortWith((x, y) => x < y)

        val len = m._2.size
        for (i <- 0 until len) {
          for (j <- i + 1 until len) {
            buffer += (((seq(i), seq(j), index), weight))
          }
        }
      }
      buffer
    }).reduceByKey((x, y) => if (x > y) x else y)
      .map(x => ((x._1._1, x._1._2), x._2))
      .reduceByKey((x, y) => x + y)
      .filter(x => x._2 >= broadcastThreshold.value)
    
    //普通多码址相似对
    val generalBlendingObjPairRDD: RDD[(Long, Long)] = generalBlendingObjPairWeightRDD.map(x => x._1)

    val uniqueValue_RowIndexList_RDD: RDD[(Long, Iterable[Long])] = uniqueValue_RowIndex_RDD.groupByKey()

    //单个特殊码址对象RDD
    val uniqueObjRDD: RDD[Long] = uniqueValue_RowIndexList_RDD.filter(m => m._2.size == 1).map(m => {
      val buffer: ArrayBuffer[Long] = new ArrayBuffer[Long]()
      m._2.copyToBuffer(buffer)
      buffer(0)
    })

    //特殊码址相似对及权重
    val uniqueBlendingObjPairWeightRDD: RDD[((Long, Long), Double)] = uniqueValue_RowIndexList_RDD.filter(m => m._2.size > 1).flatMap(m => {
      val maxThreshold = broadcastMaxThreshold.value
      val buffer: ArrayBuffer[((Long, Long), Double)] = new ArrayBuffer[((Long, Long), Double)]()
      val seq = new ArrayBuffer[Long]
      seq.appendAll(m._2)
      val len = m._2.size
      for (i <- 0 until len) {
        for (j <- i + 1 until len) {
          buffer += (((seq(i), seq(j)), maxThreshold))
        }
      }

      buffer
    })

    //特殊码址相似对
    val uniqueBlendingObjPairRDD: RDD[(Long, Long)] = uniqueBlendingObjPairWeightRDD.map(x => x._1)

    val generalEdgeRDD = generalBlendingObjPairRDD.map(x => Edge(x._1, x._2, 1))
    val generalGraph = Graph.fromEdges(generalEdgeRDD, 1L)
    val generalCC = generalGraph.connectedComponents()

    val uniqueEdgeRDD = uniqueBlendingObjPairRDD.map(x => Edge(x._1, x._2, 1))
    val uniqueGraph = Graph.fromEdges(uniqueEdgeRDD, 1L)
    val uniqueCC = uniqueGraph.connectedComponents()

    //相似的特殊码址对象long id以及(属性index, 属性值) Pair
    //格式[((属性index, value), rowIndex)] 此处的rowIndex为一群相似对象要融合到的对象的rowIndex
    val blendedUniqueObjPropertyLongID_RDD = uniqueCC.vertices.join(uniqueRowIndexRDD.map(x => (x._2, x._1))).flatMap(x => {
      val longId = x._2._1
      val pts = x._2._2._2
      val buffer = new ArrayBuffer[((Int, Long), Long)]()
      val size = pts.size
      for (i <- 1 until size) {
        buffer += ((pts(i), longId))
      }
      buffer
    })

    //单个特殊码址对象long id以及(属性index, 属性值) Pair
    //格式[((属性index, value), rowIndex)]
    val singleUniqueObjPropertyLongID_RDD = uniqueObjRDD.map(x => (x, 0)).join(uniqueRowIndexRDD.map(x => (x._2, x._1))).flatMap(x => {
      val longId = x._1
      val pts = x._2._2._2
      val buffer = new ArrayBuffer[((Int, Long), Long)]()
      val size = pts.size
      for (i <- 1 until size) {
        buffer += ((pts(i), longId))
      }
      buffer
    })

    val allUniqueObjPropertyLongID_RDD = blendedUniqueObjPropertyLongID_RDD.union(singleUniqueObjPropertyLongID_RDD)

    //相似的普通码址对象long id以及(属性index, 属性值) Pair
    //格式[((属性index, value), rowIndex)] 此处的rowIndex为一群相似对象要融合到的对象的rowIndex
    val blendedGeneralObjPropertyLongID_RDD = generalCC.vertices.join(generalRowIndexRDD.map(x => (x._2, x._1))).flatMap(x => {
      val longId = x._2._1
      val pts = x._2._2._2
      val buffer = new ArrayBuffer[((Int, Long), Long)]()
      val size = pts.size
      for (i <- 0 until size) {
        buffer += ((pts(i), longId))
      }
      buffer
    })

    //单个特殊码址对象long id以及(属性index, 属性值) Pair
    //格式[((属性index, value), rowIndex)]
    val singleGeneralPropertyLongID_RDD = generalRowIndexRDD.map(x => (x._2, 0)).subtract(generalCC.vertices.map(x => (x._1, 0)))
      .join(generalRowIndexRDD.map(x => (x._2, x._1))).flatMap(x => {
      val longId = x._1
      val pts = x._2._2._2
      val buffer = new ArrayBuffer[((Int, Long), Long)]()
      val size = pts.size
      for (i <- 0 until size) {
        buffer += ((pts(i), longId))
      }
      buffer
    })

    val allGeneralObjPropertyLongID_RDD = blendedGeneralObjPropertyLongID_RDD.union(singleGeneralPropertyLongID_RDD)

    val blendedObjectPairWeight: RDD[((Long, Long), Double)] = allUniqueObjPropertyLongID_RDD.join(allGeneralObjPropertyLongID_RDD).map(x => {
      val weightMap = broadcastPropertyWeightMap.value
      val index = x._1._1
      val weight = weightMap.getOrElse(index, 0d)

      ((x._2._1, x._2._2, index), weight)
    }).reduceByKey((x, y) => if (x > y) x else y)
      .map(x => ((x._1._1, x._1._2), x._2))
      .reduceByKey((x, y) => x + y)
      .filter(x => x._2 >= broadcastThreshold.value)

    //生成单码址对象数量存储路径
    val single_field_fusion_num_path = FileUtils.singleFieldFusionNumPath(config.address, config.fsID)
    sc.parallelize(Seq(uniqueValue_RowIndexList_RDD.count())).coalesce(1).saveAsTextFile(single_field_fusion_num_path)

    //生成相似对分布存储路径
    val similar_pair_distribution_path = FileUtils.similarPairDistributionPath(config.address, config.fsID)
    var allObjectPairWeight: RDD[((Long, Long), Double)] = sc.emptyRDD[((Long, Long), Double)]
    allObjectPairWeight = allObjectPairWeight.union(generalBlendingObjPairWeightRDD)
    allObjectPairWeight = allObjectPairWeight.union(uniqueBlendingObjPairWeightRDD)
    allObjectPairWeight = allObjectPairWeight.union(blendedObjectPairWeight)

    var distributionBuffer: ArrayBuffer[String] = new ArrayBuffer[String]()
    if (allObjectPairWeight.map(x => x._2).isEmpty()) {
      distributionBuffer ++= generateEmptyRange(threshold, maxThreshold)
    } else {
      scala.collection.immutable.Range.Double.apply(0, 1, 0.1)
      val distributionRes = allObjectPairWeight.map(x => x._2).histogram(10)
      val rangeDots = distributionRes._1
      val rangeStats = distributionRes._2
      if (rangeStats.length == 1) {
        distributionBuffer ++= generateOneRange(threshold, maxThreshold, rangeDots(0), rangeStats(0))
      } else {
        for (i <- 0 until rangeStats.length) {
          distributionBuffer += rangeDots(i) + ":" + rangeDots(i + 1) + ":" + rangeStats(i)
        }
      }
    }
    sc.parallelize(distributionBuffer).coalesce(1).saveAsTextFile(similar_pair_distribution_path)

    var blendedEdgeRDD = blendedObjectPairWeight
      .map(x => (x._1._2, (x._1._1, x._2))).reduceByKey((x, y) => {
      if (x._2 > y._2) {
        x
      } else {
        y
      }
    }).map(x => Edge(x._1, x._2._1, 1))

    //生成相似对保存地址
    val similarPairPath = FileUtils.similarPairPath(config.address, config.fsID)

    blendedEdgeRDD = blendedEdgeRDD.union(generalEdgeRDD)
    blendedEdgeRDD = blendedEdgeRDD.union(uniqueEdgeRDD)
    blendedEdgeRDD = blendedEdgeRDD.union(handledObjectPairRDD.map(x => Edge(x._1, x._2, 1)))
    val blendedGraph = Graph.fromEdges(blendedEdgeRDD, 1, edgeStorageLevel = StorageLevel.MEMORY_AND_DISK)
    val blendedCC = blendedGraph.connectedComponents()
    val blendedCCVerticesRDD = blendedCC.vertices
    val similarObjectGroupRDD = blendedCCVerticesRDD.join(allObjectIndexRDD).map(x => (x._2._1, x._2._2)).groupByKey()
    val similarObjectPairRDD = similarObjectGroupRDD.flatMap(x => {
      val objBuffer: ArrayBuffer[String] = new ArrayBuffer[String]()
      objBuffer.appendAll(x._2)

      val appendToObject = objBuffer(0)

      val size = objBuffer.size
      val pairBuffer: ArrayBuffer[(String, String)] = new ArrayBuffer[(String, String)]()

      for (i <- 1 until size) {
        pairBuffer += ((appendToObject, objBuffer(i)))
      }

      pairBuffer
    })

    //计算相似对数量
    val similarObjectPairCount = similarObjectPairRDD.count()

    //记录参与融合对象数量
    val objectCountPath = FileUtils.objectCountPath(config.address, config.fsID)
    val objectCount = blendedCCVerticesRDD.map(x => x._1).union(uniqueObjRDD).distinct().count()
    sc.parallelize(Seq(objectCount - similarObjectPairCount)).coalesce(1).saveAsTextFile(objectCountPath)

    //所有参与融合的对象Index RDD
    val blendedObjIndexRDD = blendedCCVerticesRDD.flatMap(x => Seq(x._1, x._2))

    //剩余未参与融合的单码址对象Index RDD
    val remainedUniqueObjIndexRDD = uniqueObjRDD.subtract(blendedObjIndexRDD)

    //剩余未参与融合的单码址对象自身相似对
    val remainedSelfSimilarUniqueObjRDD = remainedUniqueObjIndexRDD.map(x => (x, 1)).join(allObjectIndexRDD).map(x => (x._2._2, x._2._2))

    //包含自身对自身的相似对
    val blendedSimilarObjPairRDD = similarObjectGroupRDD.flatMap(x => {
      val objBuffer: ArrayBuffer[String] = new ArrayBuffer[String]()
      objBuffer.appendAll(x._2)

      val appendToObject = objBuffer(0)

      val size = objBuffer.size
      val pairBuffer: ArrayBuffer[(String, String)] = new ArrayBuffer[(String, String)]()

      pairBuffer += ((appendToObject, appendToObject))
      for (i <- 1 until size) {
        pairBuffer += ((appendToObject, objBuffer(i)))
      }

      pairBuffer
    })

    blendedSimilarObjPairRDD.union(remainedSelfSimilarUniqueObjRDD).saveAsTextFile(similarPairPath)
  }
}