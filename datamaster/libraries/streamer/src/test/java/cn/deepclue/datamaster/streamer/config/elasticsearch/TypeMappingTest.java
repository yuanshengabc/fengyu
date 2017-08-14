package cn.deepclue.datamaster.streamer.config.elasticsearch;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by magneto on 17-4-6.
 */
public class TypeMappingTest {
    @Test public void textMapping() throws IOException {
        TypeMapping.Text text = new TypeMapping.Text();
        XContentBuilder builder = text.mapping();
        /*
        assertEquals("{\"type\":\"string\",\"copy_to\":\"info\",\"fields\":{\"raw\":{\"type\":\"string\",\"index\":\"not_analyzed\"}}}",
            builder.string());
            */
        assertEquals("{\"type\":\"string\",\"analyzer\":\"unigram_analyzer\",\"copy_to\":\"info\",\"fields\":{\"raw\":{\"type\":\"string\",\"index\":\"not_analyzed\"}}}",
                builder.string());
    }

    @Test public void dateMapping() throws IOException {
        TypeMapping.Date dateMapping = new TypeMapping.Date();
        XContentBuilder builder = dateMapping.mapping();
        assertEquals("{\"type\":\"date\",\"copy_to\":\"info\"}", builder.string());
    }

    @Test public void intMapping() throws IOException {
        TypeMapping.Int intMapping = new TypeMapping.Int();
        XContentBuilder builder = intMapping.mapping();
        assertEquals("{\"type\":\"integer\",\"copy_to\":\"info\"}", builder.string());
    }

    @Test public void longMapping() throws IOException {
        TypeMapping.Long longMapping = new TypeMapping.Long();
        XContentBuilder builder = longMapping.mapping();
        assertEquals("{\"type\":\"long\",\"copy_to\":\"info\"}", builder.string());
    }

    @Test public void floatMapping() throws IOException {
        TypeMapping.Float floatMapping = new TypeMapping.Float();
        XContentBuilder builder = floatMapping.mapping();
        assertEquals("{\"type\":\"float\",\"copy_to\":\"info\"}", builder.string());
    }

    @Test public void doubleMapping() throws IOException {
        TypeMapping.Double doubleMapping = new TypeMapping.Double();
        XContentBuilder builder = doubleMapping.mapping();
        assertEquals("{\"type\":\"double\",\"copy_to\":\"info\"}", builder.string());
    }

}
