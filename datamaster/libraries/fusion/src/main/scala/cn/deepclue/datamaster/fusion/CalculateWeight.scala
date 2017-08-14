package cn.deepclue.datamaster.fusion

import cn.deepclue.datamaster.streamer.io.SchemaConverter
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by lah on 2017/5/10.
  */
object CalculateWeight {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf(true).setAppName("CalcWeight").set("spark.hadoop.validateOutputSpecs", "false")
    val sc = new SparkContext(conf)
    val ss = new SQLContext(sc)

    //获取配置信息
    val config = CommonUtils.readParam()

    //码址属性序列
    val propertyTypeNames: ArrayBuffer[String] = config.fields
    val globalPropertyTypes = sc.broadcast(propertyTypeNames)

    var allPropertyIndexValueRDD: RDD[((Int, Long), Long)] = sc.emptyRDD[((Int, Long), Long)]

    //表数据地址列表
    val tablePaths: ListBuffer[String] = FileUtils.readTablePaths(sc, config.address, FileUtils.tablePath(config.fsID))
    for (i <- tablePaths.indices) {
      val path = tablePaths(i)

      //读取avro文件，生成DataFrame
      val df = ss.read.format("com.databricks.spark.avro").load(path)

      //注册临时表
      val tmpTabName = "tmp" + i
      df.registerTempTable(tmpTabName)

      //查询表数据
      val sql = "select uuid" + CommonUtils.makeSelectField(df.columns, propertyTypeNames) + " from " + tmpTabName
      val rowRDD = ss.sql(sql)

      //广播columns信息
      val col_b = sc.broadcast(rowRDD.columns)

      //构造属性值-属性名称键值对
      val propertyIndexValueRDD: RDD[((Int, Long), Long)] = rowRDD.rdd.flatMap(r => {
        val buffer = new ArrayBuffer[((Int, Long), Long)]

        val cols = col_b.value
        val colNum = cols.size
        val pts = globalPropertyTypes.value

        for (i <- 1 until colNum) {
          if (r(i) != null && !r(i).toString.isEmpty) {
            val colName = SchemaConverter.base32Decode(cols(i).substring(1))
            val colValue = CommonUtils.HashStringToLong(r(i).toString)
            val colIndex = CommonUtils.indexOf(pts, colName)

            buffer += (((colIndex, colValue), 1))
          }
        }

        buffer
      })

      allPropertyIndexValueRDD = allPropertyIndexValueRDD.union(propertyIndexValueRDD)
    }

    //从其他fusion store数据源读取表信息
    val fusionWSLen = if (config.fusionKeys == null) 0 else config.fusionKeys.size
    if (fusionWSLen > 0) {
      for (i <- 0 until fusionWSLen) {
        val fusionTablePaths: ListBuffer[String] = FileUtils.readTablePaths(sc, config.address, FileUtils.tablePath(config.fusionKeys(i)))

        for (j <- fusionTablePaths.indices) {
          val path = fusionTablePaths(j)

          //读取avro文件，生成DataFrame
          val df = ss.read.format("com.databricks.spark.avro").load(path)

          //注册临时表
          val tmpTabName = "tmp" + i + j
          df.registerTempTable(tmpTabName)

          //查询表数据
          val sql = "select uuid" + CommonUtils.makeSelectField(df.columns, propertyTypeNames) + " from " + tmpTabName
          val rowRDD = ss.sql(sql)

          //广播columns信息
          val col_b = sc.broadcast(rowRDD.columns)

          //构造属性值-属性名称键值对
          val propertyIndexValueRDD: RDD[((Int, Long), Long)] = rowRDD.rdd.flatMap(r => {
            val buffer = new ArrayBuffer[((Int, Long), Long)]

            val cols = col_b.value
            val colNum = cols.size
            val pts = globalPropertyTypes.value

            for (i <- 1 until colNum) {
              if (r(i) != null && !r(i).toString.isEmpty) {
                val colName = SchemaConverter.base32Decode(cols(i).substring(1))
                val colValue = CommonUtils.HashStringToLong(r(i).toString)
                val colIndex = CommonUtils.indexOf(pts, colName)

                buffer += (((colIndex, colValue), 1))
              }
            }

            buffer
          })

          allPropertyIndexValueRDD = allPropertyIndexValueRDD.union(propertyIndexValueRDD)
        }
      }
    }

    val propertyTypeIndex_value_CountRDD = allPropertyIndexValueRDD.reduceByKey((x, y) => x + y)

    val propertyTypeIndex_totalCountMap: Map[Int, Long] = propertyTypeIndex_value_CountRDD.map(x => (x._1._1, x._2)).reduceByKey((x, y) => x + y).collect().toMap

    val globalPropertyTypeIndex_totalCountMap = sc.broadcast(propertyTypeIndex_totalCountMap)

    val sortedPropertyTypeIndex_entropyArray: Array[(Int, Double)] = propertyTypeIndex_value_CountRDD.map(x => {
      val tmpPropertyType_totalCountMap: Map[Int, Long] = globalPropertyTypeIndex_totalCountMap.value
      val totalCount = tmpPropertyType_totalCountMap.getOrElse(x._1._1, 0L)

      var probability: Double = 0.0
      if (totalCount != 0) {
        probability = x._2.toDouble / totalCount.toDouble
      } else {
        probability = 1.0
      }

      val tmp = (-probability) * Math.log(probability)
      (x._1._1, tmp)
    }).reduceByKey((x, y) => x + y).collect().sortBy(_._2)

    // 每个属性的熵的和，要对每个属性的熵归一化，才能作为权重
    var sumOfEntropy: Double = 0.00001
    for (i <- sortedPropertyTypeIndex_entropyArray.indices) {
      sumOfEntropy += sortedPropertyTypeIndex_entropyArray(i)._2
    }

    // 属性类型index，属性权重
    val propertyTypeIndex_propertyTypeWeightArray: Array[(Int, Double)] =
      sortedPropertyTypeIndex_entropyArray.map(x => (x._1, x._2 / sumOfEntropy))

    val weightPath = FileUtils.weightPath(config.address, config.fsID)

    val propertyNameWeight:ArrayBuffer[String] = new ArrayBuffer[String]()
    for (i <- 0 until propertyTypeIndex_propertyTypeWeightArray.size) {
      val item = propertyTypeIndex_propertyTypeWeightArray(i)
      propertyNameWeight += propertyTypeNames(item._1)
      propertyNameWeight += item._2.toString
    }

    sc.parallelize(propertyNameWeight).coalesce(1).saveAsTextFile(weightPath)
  }
}
