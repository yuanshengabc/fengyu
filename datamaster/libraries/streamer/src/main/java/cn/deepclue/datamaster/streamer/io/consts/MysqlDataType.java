package cn.deepclue.datamaster.streamer.io.consts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luoyong on 17-4-6.
 */
public enum MysqlDataType {
    DATE,
    DATETIME,
    TIME,
    TIMESTAMP,
    YEAR,

    BIT,
    INT,
    TINYINT,
    BIGINT,

    FLOAT,
    DECIMAL,
    DOUBLE,

    CHAR,
    SET,
    ENUM,
    LONGBLOB,
    MEDIUMTEXT,
    MEDIUMBLOB,
    SMALLINT,
    TEXT,
    BLOB,
    VARCHAR,
    LONGTEXT
   ;

    private static Map<String, MysqlDataType> nameToEnumMap;

    static {
        Map<String, MysqlDataType> map = new HashMap<>(MysqlDataType.values().length);
        for (MysqlDataType type : MysqlDataType.values()) {
            map.put(type.name(), type);
        }
        nameToEnumMap = Collections.unmodifiableMap(map);
    }

    public static MysqlDataType get(String typeName) {
        String name = typeName.indexOf('(')!= -1 ? typeName.substring(0, typeName.indexOf('(')) : typeName;
        MysqlDataType dataType = nameToEnumMap.get(name.toUpperCase());
        if (dataType == null) {
            throw new IllegalArgumentException(typeName + " is not exists");
        }
        return dataType;
    }
}
