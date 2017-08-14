package cn.deepclue.datamaster.cleaner.domain.po.fusion;

/**
 * Created by magneto on 17-5-23.
 */
public class OntologySourcePO {
    private Integer wsdsid;
    private Boolean match;
    private String multimatchPtids;

    public Integer getWsdsid() {
        return wsdsid;
    }

    public void setWsdsid(Integer wsdsid) {
        this.wsdsid = wsdsid;
    }

    public Boolean getMatch() {
        return match;
    }

    public void setMatch(Boolean match) {
        this.match = match;
    }

    public String getMultimatchPtids() {
        return multimatchPtids;
    }

    public void setMultimatchPtids(String multimatchPtids) {
        this.multimatchPtids = multimatchPtids;
    }
}
