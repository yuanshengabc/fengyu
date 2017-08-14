package cn.deepclue.datamaster.streamer.config.elasticsearch;

import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.streamer.exception.ESMappingException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by magneto on 17-4-6.
 */
@FunctionalInterface
public interface TypeMapping {

    XContentBuilder mapping();

    abstract class Type implements TypeMapping {
        public static final String RAW_CUSTOM_ALL = "info";
        protected static final Map<BaseType, TypeMapping> allMapping = new HashMap<>();

        static {
                allMapping.put(BaseType.TEXT, new Text());
                allMapping.put(BaseType.DATE, new Date());
                allMapping.put(BaseType.INT, new Int());
                allMapping.put(BaseType.LONG, new Long());
                allMapping.put(BaseType.FLOAT, new Double());
                allMapping.put(BaseType.DOUBLE, new Double());

        }

        public static Map<BaseType, TypeMapping> getAllMapping() {
            return allMapping;
        }

        @Override public XContentBuilder mapping() {
            try {
                return innerMapping();
            } catch (Exception e) {

                throw new ESMappingException(e.getMessage(), "es 对应关系异常。", e);
            }
        }

        public abstract XContentBuilder innerMapping() throws IOException;
    }

    class Text extends Type {

        @Override public XContentBuilder innerMapping() throws IOException {
            return XContentFactory.jsonBuilder().startObject()
                .field("type", "string")
                .field("analyzer", "unigram_analyzer")
                .field("copy_to", RAW_CUSTOM_ALL)
                    .startObject("fields")
                        .startObject("raw")
                            .field("type", "string")
                            .field("index", "not_analyzed")
                        .endObject()
                    .endObject()
                .endObject();
        }
    }

    class Date extends Type {

        @Override public XContentBuilder innerMapping() throws IOException {
            return XContentFactory.jsonBuilder().startObject()
                .field("type", "date")
                .field("copy_to", RAW_CUSTOM_ALL)
                .endObject();
        }
    }

    class Int extends Type {

        @Override public XContentBuilder innerMapping() throws IOException {
            return XContentFactory.jsonBuilder().startObject()
                .field("type", "integer")
                .field("copy_to", RAW_CUSTOM_ALL)
                .endObject();
        }
    }

    class Long extends Type {

        @Override public XContentBuilder innerMapping() throws IOException {
            return XContentFactory.jsonBuilder().startObject()
                .field("type", "long")
                .field("copy_to", RAW_CUSTOM_ALL)
                .endObject();
        }
    }

    class Float extends Type {

        @Override public XContentBuilder innerMapping() throws IOException {
            return XContentFactory.jsonBuilder().startObject()
                .field("type", "float")
                .field("copy_to", RAW_CUSTOM_ALL)
                .endObject();
        }
    }

    class Double extends Type {

        @Override public XContentBuilder innerMapping() throws IOException {
            return XContentFactory.jsonBuilder().startObject()
                .field("type", "double")
                .field("copy_to", RAW_CUSTOM_ALL)
                .endObject();
        }
    }
}
