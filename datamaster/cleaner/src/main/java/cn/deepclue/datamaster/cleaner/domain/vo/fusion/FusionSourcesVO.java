package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import java.util.List;

/**
 * Created by magneto on 17-6-6.
 */
public class FusionSourcesVO {
    private String hdfs;
    private List<FusionSourceVO> fusionSources;

    public String getHdfs() {
        return hdfs;
    }

    public void setHdfs(String hdfs) {
        this.hdfs = hdfs;
    }

    public List<FusionSourceVO> getFusionSources() {
        return fusionSources;
    }

    public void setFusionSources(List<FusionSourceVO> fusionSources) {
        this.fusionSources = fusionSources;
    }

    public static class FusionSourceVO {
        private String fusionKey;
        private String name;
        private String description;
        private Integer dmsid;

        public Integer getDmsid() {
            return dmsid;
        }

        public void setDmsid(Integer dmsid) {
            this.dmsid = dmsid;
        }

        public String getFusionKey() {
            return fusionKey;
        }

        public void setFusionKey(String fusionKey) {
            this.fusionKey = fusionKey;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
