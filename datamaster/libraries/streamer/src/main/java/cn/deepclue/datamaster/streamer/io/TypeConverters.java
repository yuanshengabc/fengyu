package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.io.consts.MysqlDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luoyong on 17-4-6.
 */
public class TypeConverters {
    private static Logger logger = LoggerFactory.getLogger(TypeConverters.class);

    public static final Converter<BaseType, MysqlDataType> mysqlTypeConverter = baseType -> {
        MysqlDataType dataType;
        switch (baseType) {
            case DATE:
                dataType = MysqlDataType.DATETIME;
                break;
            case DOUBLE:
                dataType = MysqlDataType.DOUBLE;
                break;
            case LONG:
                dataType = MysqlDataType.BIGINT;
                break;
            case INT:
                dataType = MysqlDataType.INT;
                break;
            case FLOAT:
                dataType = MysqlDataType.FLOAT;
                break;
            case TEXT:
                dataType = MysqlDataType.TEXT;
                break;
            default:
                throw new IllegalStateException("baseType" + baseType + " mismatch");
        }
        return dataType;
    };


    private static Map<Integer, BaseType> jdbcTypeToBaseTypeMap;

    static {
        Map<Integer, BaseType> map = new HashMap<>(35);
        map.put(Types.BIGINT, BaseType.LONG);

        map.put(Types.BIT, BaseType.INT);
        map.put(Types.BOOLEAN, BaseType.INT);
        map.put(Types.INTEGER, BaseType.INT);
        map.put(Types.SMALLINT, BaseType.INT);
        map.put(Types.TINYINT, BaseType.INT);

        map.put(Types.DECIMAL, BaseType.DOUBLE);
        map.put(Types.DOUBLE, BaseType.DOUBLE);
        map.put(Types.NUMERIC, BaseType.DOUBLE);
        map.put(Types.FLOAT, BaseType.DOUBLE);

        map.put(Types.REAL, BaseType.FLOAT);

        map.put(Types.CHAR, BaseType.TEXT);
        map.put(Types.LONGNVARCHAR, BaseType.TEXT);
        map.put(Types.LONGVARCHAR, BaseType.TEXT);
        map.put(Types.NCHAR, BaseType.TEXT);
        map.put(Types.NVARCHAR, BaseType.TEXT);
        map.put(Types.VARCHAR, BaseType.TEXT);
        map.put(Types.CLOB, BaseType.TEXT);
        map.put(Types.NCLOB, BaseType.TEXT);
        map.put(Types.DATALINK, BaseType.TEXT);
        map.put(Types.SQLXML, BaseType.TEXT);
        map.put(Types.ROWID, BaseType.TEXT);

        map.put(Types.DATE, BaseType.DATE);
        map.put(Types.TIME, BaseType.DATE);
        map.put(Types.TIMESTAMP, BaseType.DATE);

//这些类型跳过不处理
//        map.put(Types.ARRAY, BaseType.TEXT);
//        map.put(Types.BINARY, BaseType.TEXT);
//        map.put(Types.BLOB, BaseType.TEXT);
//        map.put(Types.DISTINCT, BaseType.TEXT);
//        map.put(Types.JAVA_OBJECT, BaseType.TEXT);
//        map.put(Types.LONGVARBINARY, BaseType.TEXT);
//        map.put(Types.VARBINARY, BaseType.TEXT);
//        map.put(Types.NULL, BaseType.TEXT);
//        map.put(Types.OTHER, BaseType.TEXT);
//        map.put(Types.REF, BaseType.TEXT);
//        map.put(Types.STRUCT, BaseType.TEXT);
//        map.put(Types.REF_CURSOR, BaseType.TEXT);
//
//        map.put(Types.TIME_WITH_TIMEZONE, BaseType.DATE);
//        map.put(Types.TIMESTAMP_WITH_TIMEZONE, BaseType.DATE);
        jdbcTypeToBaseTypeMap = Collections.unmodifiableMap(map);
    }

    public static final Converter<Integer, BaseType> jsbcTypeConverter = dataType -> {
        BaseType baseType = jdbcTypeToBaseTypeMap.get(dataType);
        if (baseType == null) {
            logger.warn("jdbc type {}没有映射到相应的BaseType", baseType);
//            throw new IllegalStateException(dataType + "没有映射到相应的BaseType");
        }
        return baseType;
    };

    @FunctionalInterface
    public interface Converter<S, D> {
        D convert(S dataType);
    }

}
