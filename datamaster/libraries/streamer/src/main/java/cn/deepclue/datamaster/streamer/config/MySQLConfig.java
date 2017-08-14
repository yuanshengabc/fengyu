package cn.deepclue.datamaster.streamer.config;

/**
 * Created by xuzb on 29/03/2017.
 */
public class MySQLConfig implements Config {
    private String ip;
    private int port = 3306;
    private String database;
    private String username;
    private String password;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getConnectionUrl() {
        String connectionUrl = "jdbc:mysql://" + ip + ":" + port;
        String suffix =  "?" + "user=" + username + "&password=" + password +
                "&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&zeroDateTimeBehavior=convertToNull";
        if (database != null && !database.isEmpty()) {
            connectionUrl  += "/" + database + suffix;
        } else {
            connectionUrl +=  suffix;
        }

        return connectionUrl;
    }
}
