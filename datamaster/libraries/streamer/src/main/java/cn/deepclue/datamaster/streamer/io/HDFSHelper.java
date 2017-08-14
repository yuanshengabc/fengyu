package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import cn.deepclue.datamaster.streamer.config.ZkConfig;
import com.google.protobuf.InvalidProtocolBufferException;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.hadoop.hdfs.server.namenode.ha.proto.HAZKInfoProtos;

public class HDFSHelper {
    public static void fetchServerFromZK(HDFSConfig hdfsConfig) {
        ZkConfig zkConfig = hdfsConfig.getZkConfig();

        ZkClient zkClient = new ZkClient(zkConfig.getZkUrl(),
                zkConfig.getSessionTimeout(), zkConfig.getConnectionTimeout(),
                new ZkSerializer() {
                    @Override
                    public byte[] serialize(Object data) throws ZkMarshallingError {
                        return new byte[0];
                    }

                    @Override
                    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                        try {
                            return HAZKInfoProtos.ActiveNodeInfo.parseFrom(bytes);
                        } catch (InvalidProtocolBufferException e) {
                            throw new ZkMarshallingError(e);
                        }
                    }
                });
        HAZKInfoProtos.ActiveNodeInfo activeNodeInfo =
                zkClient.readData(getNodePath(hdfsConfig.getClusterName()));

        hdfsConfig.setServer(activeNodeInfo.getHostname());
        hdfsConfig.setPort(activeNodeInfo.getPort());
    }

    private static String getNodePath(String clusterName) {
        return "/hadoop-ha/" + clusterName + "/ActiveStandbyElectorLock";
    }
}
