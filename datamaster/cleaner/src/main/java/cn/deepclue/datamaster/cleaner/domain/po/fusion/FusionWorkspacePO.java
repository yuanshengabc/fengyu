package cn.deepclue.datamaster.cleaner.domain.po.fusion;

/**
 * Created by magneto on 17-5-18.
 */
public class FusionWorkspacePO {
    //工作空间id
    private Integer wsid;
    //步骤
    private Integer step;
    //业务模型id
    private Integer otid;
    //recore source id
    private Integer rsid;
    //码值类型
    private Integer addressCodeType;
    //权值计算状态
    private Integer entropyCalculationTid;
    //相似度阀值
    private Double threshold;
    //融合任务状态
    private Integer fusionTid;

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getOtid() {
        return otid;
    }

    public void setOtid(Integer otid) {
        this.otid = otid;
    }

    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }


    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Integer getAddressCodeType() {
        return addressCodeType;
    }

    public void setAddressCodeType(Integer addressCodeType) {
        this.addressCodeType = addressCodeType;
    }

    public Integer getEntropyCalculationTid() {
        return entropyCalculationTid;
    }

    public void setEntropyCalculationTid(Integer entropyCalculationTid) {
        this.entropyCalculationTid = entropyCalculationTid;
    }

    public Integer getFusionTid() {
        return fusionTid;
    }

    public void setFusionTid(Integer fusionTid) {
        this.fusionTid = fusionTid;
    }
}
