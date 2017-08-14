package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.Env;
import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.exception.ESException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by magneto on 17-4-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ESSessionTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void mapping() throws Exception {
        ESSession session = new ESSession();
        RSSchema schema = new RSSchema();
        schema.setName("test");
        RSField field1 = new RSField();
        field1.setName("strField");
        field1.setBaseType(BaseType.TEXT);
        schema.addField(field1);
        RSField field2 = new RSField();
        field2.setName("intField");
        field2.setBaseType(BaseType.INT);
        schema.addField(field2);
        String mappingStr = session.mapping(schema, "test").string();
        assertEquals("{\"test\":{\"properties\":{\"strField\":{\"type\":\"string\",\"copy_to\":\"info\",\"fields\":{\"raw\":{\"type\":\"string\",\"index\":\"not_analyzed\"}}},\"intField\":{\"type\":\"integer\",\"copy_to\":\"info\"},\"info\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"ignore_above\":200}}}}", mappingStr);
        session.close();
    }

    @Test
    public void stringMapping() {
        //保证55的es被初始化
        ESTypeConfig esTypeConfig = new ESTypeConfig();
        esTypeConfig.setClusterName("datamaster");
        esTypeConfig.setClusterIp("172.24.8.115");
        esTypeConfig.setReplicasNum(1);
        esTypeConfig.setShardsNum(2);
        esTypeConfig.setIndex(11+"");
        esTypeConfig.setType(111+"");
        ESSession esSession= new ESSession(esTypeConfig);
        String index = "ds_1_rs_1";
        String type = "ds_1_rs_1";
        boolean ret = esSession.stringMapping(index, type, "field1");
        assertTrue(ret);
        ret = esSession.stringMapping(index, type, "field2");
        assertFalse(ret);
        ret = esSession.stringMapping(index, type, "field3");
        assertFalse(ret);

        ret = esSession.stringMapping(index, "field1");
        assertTrue(ret);
        ret = esSession.stringMapping(index, "field2");
        assertFalse(ret);
        ret = esSession.stringMapping(index, "field3");
        assertFalse(ret);

        expectedException.expect(ESException.class);
        esSession.stringMapping(index, "name");

    }

    @Test
    public void unigramAnalyzer() throws IOException {
        String unigramAnalyzerStr = ESSession.unigramAnalyzer();
        System.out.println(unigramAnalyzerStr);
        String expectStr = "{\n" +
                "    \"tokenizer\": {\n" +
                "        \"unigram_tokenizer\": {\n" +
                "            \"type\": \"ngram\",\n" +
                "            \"min_gram\": 1,\n" +
                "            \"max_gram\": 1,\n" +
                "            \"token_chars\": [\n" +
                "                \"letter\",\n" +
                "                \"digit\"\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"analyzer\": {\n" +
                "        \"unigram_analyzer\": {\n" +
                "            \"tokenizer\": \"unigram_tokenizer\",\n" +
                "            \"filter\": [\n" +
                "                \"standard\",\n" +
                "                \"lowercase\"\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";
        expectStr = "{\"tokenizer\":{\"unigram_tokenizer\":{\"type\":\"ngram\",\"min_gram\":1,\"max_gram\":1,\"token_chars\":[\"letter\",\"digit\",\"symbol\",\"punctuation\"]}},\"analyzer\":{\"unigram_analyzer\":{\"tokenizer\":\"unigram_tokenizer\",\"filter\":[\"standard\",\"lowercase\"]}}}";
        assertEquals(expectStr, unigramAnalyzerStr);
    }

    @Test
    public void createIndex() {
        ESTypeConfig esTypeConfig = new ESTypeConfig();
        esTypeConfig.setClusterName("datamaster");
        esTypeConfig.setClusterIp("172.24.8.115");
        esTypeConfig.setReplicasNum(1);
        esTypeConfig.setShardsNum(2);
        esTypeConfig.setIndex("test");
        esTypeConfig.setType("type");
        ESSession esSession= new ESSession(esTypeConfig);
        if (!esSession.indexExist("test")) {
            esSession.createIndex("test");
        }
        esSession.close();
    }

    @Test
    public void waitForHealth() {
        //Mark 当index处于green状态时，waitForYellow直接通过
        ESConfig esConfig = Env.esConfig();
        final String index = "test";
        ESSession esSession= new ESSession(esConfig);
        esSession.deleteIndex(index);
        if (!esSession.indexExist(index)) {
            esSession.createIndex(index);
        }

        long t1 = System.currentTimeMillis();
        boolean isHealth = esSession.waitForHealth(index);
        assertThat(isHealth).isTrue();
        long t2 = System.currentTimeMillis();
        assertThat((t2-t1)).isLessThan(1000);

        esSession.deleteIndex(index);
        esSession.close();
    }

    @Test
    public void checkClusterStatus() {
        ESConfig esConfig = Env.esConfig();
        ESSession esSession= new ESSession(esConfig);
        boolean isHealth = esSession.checkClusterStatus(3000);
        assertThat(isHealth).isTrue();
        esSession.close();
    }

}
