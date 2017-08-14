package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.Env;
import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.session.ESSession;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by ggchangan on 17-7-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ESWriterTest {
    @Test
    public void esWriter() {
        ESConfig esConfig = Env.esConfig();
        final String index = "test";
        final String type = "type";
        ESTypeConfig esTypeConfig = new ESTypeConfig(esConfig, index, type);

        ESWriter esWriter = new ESWriter(esTypeConfig);
        esWriter.open();
        esWriter.getSession().deleteIndex(index);
        RSSchema schema = new RSSchema();
        RSField field1 = new RSField("field1", BaseType.TEXT);
        RSField field2 = new RSField("field2", BaseType.INT);
        RSField field3 = new RSField("field3", BaseType.DATE);
        RSField field4 = new RSField("field4", BaseType.FLOAT);
        schema.addField(field1);
        schema.addField(field2);
        schema.addField(field3);
        schema.addField(field4);
        esWriter.writeSchema(schema);
        List<Record> records = new ArrayList<>();
        Record record1 = new Record(schema);
        record1.setKey("1");
        record1.addValue("中国");
        record1.addValue(1);
        record1.addValue(new Date());
        record1.addValue(1.23f);
        Record record2 = new Record(schema);
        record2.setKey("2");
        record2.addValue("俄罗斯");
        record2.addValue(2);
        record2.addValue(new Date());
        record2.addValue(1.23f);
        Record record3 = new Record(schema);
        record3.setKey("3");
        record3.addValue("日本");
        record3.addValue(3);
        record3.addValue(new Date());
        record3.addValue(1.24f);
        records.add(record1);
        records.add(record2);
        records.add(record3);
        for (Record record: records) {
            esWriter.writeRecord(record);
        }

        esWriter.stopBulk();
        ESSession esSession = esWriter.getSession();
        //甚至刷新2次，在尝试20次搜索时，亦有可能不能搜索出所有数据，这是为什么，从es官方文档来看，refresh之后，所有数据应该是对
        //搜索可见了！！！
        //此时，只能先sleep 1s 使得数据可以被查询到
        //esSession.refresh(esTypeConfig.getIndex());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SearchHits searchHits = esSession.allDocs(index, type, 0, 10, null, false);
        assertThat(searchHits.getTotalHits()).isEqualTo(3);
        esWriter.close();

        ESSession session = new ESSession(esConfig);
        session.deleteIndex(index);
    }


}
