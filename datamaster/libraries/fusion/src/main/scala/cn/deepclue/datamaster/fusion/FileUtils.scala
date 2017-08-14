package cn.deepclue.datamaster.fusion

import org.apache.spark.SparkContext
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.collection.mutable.ListBuffer

/**
  * Created by lah on 2017/5/18.
  */
object FileUtils {
  val FS_DEFAULT_NAME_KEY = "fs.defaultFS"
  val SLASH = "/"
  val BASE_PATH = "/fusion"
  val TABLE_PATH = "/tables"
  val WEIGHT_PATH = "/weight"
  val SIMILAR_PAIR_DISTRIBUTION_PATH = "/similar_pair_distribution"
  val SIMILAR_PAIR_PATH = "/similar_pair"
  val SINGLE_FIELD_FUSION_NUM = "/single_field_fusion_num"
  val FUSION_KEYS_PATH = "/fusion_keys"
  val OBJECT_COUNT_PATH = "/object_count"

  def tablePath(fsId: String): String = {
    BASE_PATH + SLASH + fsId + TABLE_PATH
  }

  def basePath(address: String, fsId: String): String = {
    address + BASE_PATH + SLASH + fsId
  }

  def weightPath(address: String, fsId: String): String = {
    basePath(address, fsId) + WEIGHT_PATH
  }

  def similarPairPath(address: String, fsId: String): String = {
    basePath(address, fsId) + SIMILAR_PAIR_PATH
  }

  def similarPairDistributionPath(address: String, fsId: String): String = {
    basePath(address, fsId) + SIMILAR_PAIR_DISTRIBUTION_PATH
  }

  def singleFieldFusionNumPath(address: String, fsId: String): String = {
    basePath(address, fsId) + SINGLE_FIELD_FUSION_NUM
  }

  def fusionKeysPath(address: String, fsId: String): String = {
    basePath(address, fsId) + FUSION_KEYS_PATH
  }

  def objectCountPath(address: String, fsId: String): String = {
    basePath(address, fsId) + OBJECT_COUNT_PATH
  }

  def deleteDir(sc: SparkContext, address: String, fsID: String, dirType: Int): Unit = {
    sc.hadoopConfiguration.set(FS_DEFAULT_NAME_KEY, address)
    val fs = FileSystem.get(sc.hadoopConfiguration)
    val path = dirType match {
      case 1 => new Path(BASE_PATH + SLASH + fsID + WEIGHT_PATH)
      case 2 => new Path(BASE_PATH + SLASH + fsID + SIMILAR_PAIR_DISTRIBUTION_PATH)
      case 3 => new Path(BASE_PATH + SLASH + fsID + SIMILAR_PAIR_PATH)
    }
    if (fs.exists(path)) {
      fs.delete(path, false)
    }
  }

  def readTablePaths(sc: SparkContext, address: String, dir: String): ListBuffer[String] = {
    import org.apache.hadoop.fs.{FileSystem, Path}

    sc.hadoopConfiguration.set(FS_DEFAULT_NAME_KEY, address)
    val fs = FileSystem.get(sc.hadoopConfiguration)
    val files = scala.collection.mutable.ListBuffer.empty[String]

    val path = new Path(dir)
    if (fs.exists(path)) {
      val status = fs.listStatus(new Path(dir))
      status.foreach(x => {
        if (x.isDirectory) {
          files += x.getPath.toString
        }
      })
    }
    files
  }
}