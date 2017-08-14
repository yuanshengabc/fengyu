package cn.deepclue.datamaster.streamer.transform.transformer.field;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by lilei-mac on 2017/4/26.
 */
public class ConvertTypeTFTest {

    @Test
    public void testConvertTypeTF(){
        test(BaseType.TEXT,BaseType.INT,"10",10);
        test(BaseType.TEXT,BaseType.INT,"sss",null);
        test(BaseType.TEXT,BaseType.LONG,"10",10L);
        test(BaseType.TEXT,BaseType.LONG,"sss",null);
        test(BaseType.TEXT,BaseType.FLOAT,"10.2",10.2f);
        test(BaseType.TEXT,BaseType.FLOAT,"sss",null);
        test(BaseType.TEXT,BaseType.DOUBLE,"10.3",10.3d);
        test(BaseType.TEXT,BaseType.DOUBLE,"sss",null);
        test(BaseType.TEXT,BaseType.DATE,"1987-05-22 01:03:03",parseFormat("yyyy-MM-dd HH:mm:ss","1987-05-22 01:03:03"));
        test(BaseType.TEXT,BaseType.DATE,"1987-05-22 01:03:03 999",parseFormat("yyyy-MM-dd HH:mm:ss S","1987-05-22 01:03:03 999"));
        test(BaseType.TEXT,BaseType.DATE,"1987年05月22日 01时03分03秒",parseFormat("yyyy年MM月dd日 HH时mm分ss秒","1987年05月22日 01时03分03秒"));
        test(BaseType.TEXT,BaseType.DATE,"1987年05月22日 01:03:03 999",parseFormat("yyyy年MM月dd日 HH:mm:ss S","1987年05月22日 01:03:03 999"));
        test(BaseType.TEXT,BaseType.DATE,"1987/05/22 01:03:03 999",parseFormat("yyyy/MM/dd HH:mm:ss S","1987/05/22 01:03:03 999"));
        test(BaseType.TEXT,BaseType.DATE,"1987 05 22 01:03:03 999",parseFormat("yyyy MM dd HH:mm:ss S","1987 05 22 01:03:03 999"));
        test(BaseType.TEXT,BaseType.DATE,"1987-05-22",parseFormat("yyyy-MM-dd","1987-05-22"));
        test(BaseType.TEXT,BaseType.DATE,"19870522",parseFormat("yyyyMMdd","19870522"));
        test(BaseType.TEXT,BaseType.DATE,"sss",null);

        test(BaseType.INT,BaseType.TEXT,10,"10");
        test(BaseType.INT,BaseType.LONG,10,10L);
        test(BaseType.INT,BaseType.FLOAT,10,10f);
        test(BaseType.INT,BaseType.DOUBLE,10,10d);
        test(BaseType.INT,BaseType.DATE,1000,new Date(1000*1000));

        test(BaseType.LONG,BaseType.TEXT,10L,"10");
        test(BaseType.LONG,BaseType.INT,10L,10);
        test(BaseType.LONG,BaseType.FLOAT,10L,10f);
        test(BaseType.LONG,BaseType.DOUBLE,10L,10d);
        test(BaseType.LONG,BaseType.DATE,10L,new Date(10*1000));
        test(BaseType.LONG,BaseType.DATE,10000000000L,new Date(10000000000L));

        test(BaseType.FLOAT,BaseType.TEXT,10f,"10");
        test(BaseType.FLOAT,BaseType.TEXT,10.1f,"10.1");
        test(BaseType.FLOAT,BaseType.INT,10.1f,10);
        test(BaseType.FLOAT,BaseType.LONG,10.1f,10L);
        test(BaseType.FLOAT,BaseType.DOUBLE,10.10f,(double) 10.10f);

        test(BaseType.DOUBLE,BaseType.TEXT,10d,"10");
        test(BaseType.DOUBLE,BaseType.TEXT,10.10d,"10.1");
        test(BaseType.DOUBLE,BaseType.INT,10.11d,10);
        test(BaseType.DOUBLE,BaseType.LONG,10.11d,10L);
        test(BaseType.DOUBLE,BaseType.FLOAT,10.11d,10.11f);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        test(BaseType.DATE, BaseType.TEXT, new Date(10000000000L), simpleDateFormat.format(new Date(10000000000L)));
        test(BaseType.DATE,BaseType.LONG,new Date(10000000000L),10000000000L);
    }

    private void test(BaseType sourceType, BaseType targetType, Object sourceValue, Object targetValue) {

        RSSchema schema = new RSSchema();
        RSField field = new RSField("testField", sourceType);
        schema.addField(field);

        Record record = new Record(schema);
        record.addValue(sourceValue);

        ConvertTypeTF tf=new ConvertTypeTF("testField", targetType);

        RSSchema targetSchema = tf.prepareSchema(schema);
        Record targetRecord = tf.transform(record);
        assertEquals(targetSchema.count(), schema.count());
        assertEquals(targetSchema.getField(0).getName(), schema.getField(0).getName());
        assertEquals(record.size(), targetRecord.size());

        Object newValue = targetRecord.getValue(0);
        assertEquals(targetValue, newValue);
    }

    private Date parseFormat(String format,String text) {

        Date result = null;
        try {
            result = new SimpleDateFormat(format).parse(text);
        } catch (ParseException ignored) {
        }
        return result;
    }
}
