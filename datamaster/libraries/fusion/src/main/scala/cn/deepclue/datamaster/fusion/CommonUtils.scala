package cn.deepclue.datamaster.fusion

import java.io.{BufferedReader, InputStreamReader}

import cn.deepclue.datamaster.streamer.io.SchemaConverter
import org.json.JSONObject

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by lah on 2017/5/19.
  */
object CommonUtils {
  val FS_DEFAULT_NAME_KEY = "fs.defaultFS"

  val ADDRESS = "address"
  val FUSION_ID = "fsid"
  val FIELDS = "fields"
  val THRESHOLD = "threshold"
  val SPECIAL_FIELD = "specialField"
  val FIELD_WEIGHTS = "fieldWeights"
  val FUSION_KEYS = "fusionKeys"

  def indexOf(propertyTypes: ArrayBuffer[String], name: String): Int = {
    if (name.length <=0 ) {
      return -1
    }

    var index = name.length - 1
    var ch = name.charAt(index)
    while (ch >= '0' && ch <= '9') {
      index = index - 1
      ch = name.charAt(index)
    }

    val namePrefix = name.substring(0, index + 1)
    propertyTypes.indexOf(namePrefix)

  }

  def contains(columns: Array[String], name: String): Boolean = {
    if (columns.indexWhere(c => c.equals(name)) < 0) {
      false
    } else {
      true
    }
  }

  def makeSelectField(columns: Array[String], weightColumns: ArrayBuffer[String]): String = {
    if (weightColumns == null || weightColumns.length == 0) {
      return ""
    }

    val sb: StringBuilder = new StringBuilder
    for (c <- columns) {
      val originColumn = SchemaConverter.base32Decode(c.substring(1))
      if (indexOf(weightColumns, originColumn) >= 0) {
        sb ++= "," + c
      }
    }
    sb.toString()
  }

  def HashStringToLong(inputString: String): Long = {
    var ret: Long = 1125899906842597L
    val inputStringLength: Int = inputString.length
    for ( i <- 0 until inputStringLength) {
      ret = 31 * ret + inputString.charAt(i)
    }
    ret
  }

  def readParam(): SimilarPairTaskConfig = {
    val reader: BufferedReader = new BufferedReader(new InputStreamReader(System.in, "UTF8"))
    val sb: StringBuilder = new StringBuilder
    var s: String = ""
    while (s != null) {
      s = reader.readLine
      if (s != null) sb.append(s)
    }
    val paramObj = new JSONObject(sb.toString())

    val config = new SimilarPairTaskConfig
    config.fsID = paramObj.getString(FUSION_ID)
    config.address = paramObj.getString(ADDRESS)
    config.threshold = paramObj.optDouble(THRESHOLD)
    config.specialField = paramObj.optString(SPECIAL_FIELD)

    val fieldWeights = paramObj.optJSONArray(FIELD_WEIGHTS)
    val fieldBuf: ArrayBuffer[String] = new ArrayBuffer[String]()
    val weightMap: mutable.HashMap[Int, Double] = new mutable.HashMap[Int, Double]()
    if (fieldWeights != null && fieldWeights.length() > 0) {
      val len = fieldWeights.length()
      for (i <- 0 until len) {
        val fieldWeight = fieldWeights.getJSONObject(i)
        weightMap.put(i, fieldWeight.getDouble("weight"))
        fieldBuf += fieldWeight.getString("name")
      }
    }
    config.fieldWeights = weightMap
    config.fields = fieldBuf

    val fields = paramObj.optJSONArray(FIELDS)
    if (fields != null && fields.length() > 0) {
      val len = fields.length()
      val fieldBuf: ArrayBuffer[String] = new ArrayBuffer[String]()
      for (i <- 0 until len) {
        fieldBuf += fields.getString(i)
      }
      config.fields = fieldBuf
    }

    val fusionKeys = paramObj.optJSONArray(FUSION_KEYS)
    val fusionKeyBuf = new ArrayBuffer[String]()
    if (fusionKeys != null && fusionKeys.length() > 0) {
      val len = fusionKeys.length()
      for (i <- 0 until len) {
        fusionKeyBuf += fusionKeys.getString(i)
      }
    }
    config.fusionKeys = fusionKeyBuf

    config
  }

  class SimilarPairTaskConfig {
    var fsID:String = null
    var address:String = null
    var fields:ArrayBuffer[String] = null
    var threshold:Double = 0.0d
    var specialField:String = null
    var fieldWeights:mutable.HashMap[Int, Double] = null
    var fusionKeys:ArrayBuffer[String] = null
  }
}
