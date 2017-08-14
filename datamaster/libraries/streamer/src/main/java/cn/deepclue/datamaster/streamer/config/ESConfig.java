package cn.deepclue.datamaster.streamer.config;

/**
 * Created by xuzb on 29/03/2017.
 */
public class ESConfig implements Config {
    private String clusterName;
    private String clusterIp;
    private int shardsNum;
    private int replicasNum;
    private int parallelism;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public int getShardsNum() {
        return shardsNum;
    }

    public void setShardsNum(int shardsNum) {
        this.shardsNum = shardsNum;
    }

    public int getReplicasNum() {
        return replicasNum;
    }

    public void setReplicasNum(int replicasNum) {
        this.replicasNum = replicasNum;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }
}
