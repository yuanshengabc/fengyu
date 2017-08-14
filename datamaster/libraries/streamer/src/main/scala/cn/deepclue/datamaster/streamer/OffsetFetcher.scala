package cn.deepclue.datamaster.streamer

import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo}
import kafka.common.TopicAndPartition
import kafka.consumer.SimpleConsumer
import org.apache.kafka.common.TopicPartition

/**
 * Created by xuzb on 28/03/2017.
 */
class OffsetFetcher(val host: String, val port: Int) {

  private val consumer = new SimpleConsumer(host, port, 10000, 100000, "GetOffsetShell")

  def fetch(topicPartition: TopicPartition): Long = {
    val topicAndPartition = TopicAndPartition(topicPartition.topic(), topicPartition.partition());
    val request = OffsetRequest(Map(topicAndPartition -> PartitionOffsetRequestInfo(-1, 1)))
    val offsets = consumer.getOffsetsBefore(request).partitionErrorAndOffsets(topicAndPartition).offsets
    if (offsets.nonEmpty) {
      return offsets.head
    }
    0
  }
}
