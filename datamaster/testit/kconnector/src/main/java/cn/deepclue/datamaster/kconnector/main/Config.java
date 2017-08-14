package cn.deepclue.datamaster.kconnector.main;

public class Config {
    private String kafkaBinPath;
    private String workerPath;
    private String dbPath;

    public String getKafkaBinPath() {
        return this.kafkaBinPath;
    }

    public void setKafkaBinPath(String kafkaBinPath) {
        this.kafkaBinPath=kafkaBinPath;
    }

    public String getWorkerPath() {
        return this.workerPath;
    }

    public void setWorkerPath(String workerPath) {
        this.workerPath = workerPath;
    }

    public String getDbPath() {
        return this.dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath =dbPath;
    }

    public String toString(){
        return "db_path="+this.dbPath+"\n"
                +"worker_path="+this.workerPath+"\n"
                +"kafka_bin_path"+this.kafkaBinPath;
    }
}
