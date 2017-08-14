package cn.deepclue.datamaster.cleaner.domain.bo.data;

import java.util.Date;

/**
 * Created by xuzb on 17/03/2017.
 * Imported data record source.
 */
public class RecordSource {
    private Integer rsid;
    private String name;
    private Integer dsid;
    private Integer type;
    private Date createdOn;

    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public RSType getRSType() {
        return RSType.getRSType(type);
    }

    public void setRSType(RSType type) {
        this.type = type.value;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getESIndexName() {
        return "ds_" + getDsid() + "_rs_" + getRsid();
    }

    public String getESTypeName() {
        return "ds_" + getDsid() + "_rs_" + getRsid();
    }

    public String getKafkaTopic() {
        return "ds-" + getDsid() + "-rs-" + getRsid() + "-" + (createdOn == null ? "" : createdOn.getTime());
    }

    public String getFusionKey() {
        return "rs-" + getRsid() + "-" + (createdOn == null ? "" : createdOn.getTime());
    }

    public String getHDFSFilePath() {
        return "/cleaning/" + "rs-" + getRsid();
    }

    public static Integer getRsid(String fusionKey) {
        if (fusionKey == null || fusionKey.isEmpty() || !fusionKey.contains("-")) {
            throw new IllegalStateException("illegal fusion key: " + fusionKey);
        }

        String rsidStr = fusionKey.substring(fusionKey.indexOf('-'), fusionKey.lastIndexOf('-'));
        if (rsidStr == null || rsidStr.isEmpty()) {
            throw new IllegalStateException("illegal fusion key: " + fusionKey);
        }

        return Integer.valueOf(rsidStr);
    }

    public enum RSType {
        MYSQL(0), FUSION(1);

        private int value;

        RSType(int value) {
            this.value = value;
        }

        public static RSType getRSType(int type) {
            switch (type) {
                case 0:
                    return MYSQL;

                case 1:
                    return FUSION;

                default:
                    throw new IllegalStateException("Unknown rs type: " + type);
            }
        }
    }
}
