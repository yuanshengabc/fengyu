package cn.deepclue.datamaster.streamer.config;

/**
 * Created by magneto on 17-4-7.
 */
public class ESTypeConfig extends ESConfig{
    private String index;
    private String type;

    public ESTypeConfig(){
        //Do nothing because of allowing empty & fill with get & set
    }

    public ESTypeConfig(ESConfig esConfig, String index, String type) {
        this.setClusterName(esConfig.getClusterName());
        this.setClusterIp(esConfig.getClusterIp());
        this.setReplicasNum(esConfig.getReplicasNum());
        this.setShardsNum(esConfig.getShardsNum());
        this.setParallelism(esConfig.getParallelism());
        this.index = index;
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
