package cn.deepclue.datamaster.streamer.session;


import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.elasticsearch.TypeMapping;
import cn.deepclue.datamaster.streamer.exception.ESException;
import cn.deepclue.datamaster.streamer.exception.ESMappingException;
import cn.deepclue.datamaster.streamer.exception.StreamerException;
import com.carrotsearch.hppc.ObjectContainer;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by magneto on 17-4-6.
 */
public class ESSession {
    private static Logger logger = LoggerFactory.getLogger(ESSession.class);

    private ESConfig esConfig;
    private Client client;

    public ESSession() {
    }

    public ESSession(ESConfig esConfig) {
        this.esConfig = esConfig;
        this.client = getClient();
    }

    public Client getClient() {
        if (client != null) {
            return client;
        }

        if (esConfig == null) {
            throw new ESException("es server config exception.", "es服务器配置异常！");
        }

        String ipaddr = esConfig.getClusterIp();
        String clusterName = esConfig.getClusterName();
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", true)
                .put("client.transport.ping_timeout", 120, TimeUnit.SECONDS)
                .build();

        client = TransportClient.builder().settings(settings).build().addTransportAddress(
                new InetSocketTransportAddress(new InetSocketAddress(ipaddr, 9300)));
        return client;
    }

    public void createIndex(String indexName) {
        CreateIndexResponse cir = null;
        try {
            XContentParser parser = JsonXContent.jsonXContent.createParser(unigramAnalyzer());
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("number_of_replicas", esConfig.getReplicasNum());
            builder.field("number_of_shards", esConfig.getShardsNum());
            builder.field("analysis");
            builder.copyCurrentStructure(parser);
            builder.endObject();
            Settings.Builder settings = Settings.settingsBuilder().loadFromSource(builder.string());
            cir = client.admin().indices().prepareCreate(indexName)
                    .setSettings(settings).setMasterNodeTimeout(TimeValue.timeValueMinutes(1))
                    .execute().actionGet();
        } catch (IOException e) {
            throw new ESException(e.getMessage(), "创建索引异常！", e);
        }

        if (cir == null || !cir.isAcknowledged()) {
            throw new StreamerException(String.format("Failed to create index %s.", indexName), String.format("创建索引%s失败。", indexName));
        }
    }

    public static String unigramAnalyzer() {
        XContentBuilder settingsBuilder = null;
        try {
            settingsBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("tokenizer")
                    .startObject("unigram_tokenizer")
                    .field("type", "ngram")
                    .field("min_gram", 1)
                    .field("max_gram", 1)
                    .array("token_chars", new String[]{"letter", "digit", "symbol", "punctuation"})
                    .endObject()
                    .endObject()
                    .startObject("analyzer")
                    .startObject("unigram_analyzer")
                    .field("tokenizer", "unigram_tokenizer")
                    .array("filter", new String[]{"standard", "lowercase"})
                    .endObject()
                    .endObject()
                    .endObject();

            return settingsBuilder.string();
        } catch (IOException e) {
            throw new ESException(e.getMessage(), "uniGram分词器创建失败！", e);
        }
    }

    public SearchHits allDocs(String index, String type, int page, int pageSize, String orderBy, boolean asc) {
        QueryBuilder searchQuery = QueryBuilders.matchAllQuery();
        return allDocs(index, type, searchQuery, page, pageSize, orderBy, asc);
    }

    public SearchHits allDocs(String index, String type, QueryBuilder query, int page, int pageSize, String orderBy, boolean asc) {
        int from = page * pageSize;
        int size = pageSize;

        SortBuilder sortBuilder = null;
        if (orderBy != null && !orderBy.isEmpty()) {
            sortBuilder = SortBuilders.fieldSort(orderBy);
            sortBuilder.order(asc ? SortOrder.ASC : SortOrder.DESC);
        }

        SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type).setQuery(query);
        if (sortBuilder != null) srb.addSort(sortBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = srb.setFrom(from).setSize(size).execute().actionGet();
        } catch (Exception e) {
            if (query.toString().contains("regex") && e.getCause().getCause() instanceof IllegalArgumentException) {
                throw new ESException(e.getCause().getMessage(), "正则表达式异常！", e);
            }
            throw new ESException(e.getMessage(), "查询异常！", e);
        }

        return searchResponse.getHits();
    }


    public long totalDocs(String index, String type) {
        QueryBuilder searchQuery = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                .setQuery(searchQuery).execute().actionGet();

        return searchResponse.getHits().getTotalHits();
    }

    /**
     * 下列情况被认为为空
     * { "user": null }
     * { "user": [] }
     * { "user": [null] }
     * { "foo":  "bar" } 不含有"user"
     * 这四种情况使用missinQuery解决，对于字符串中的空串
     * 使用termQuery对不分词域进行统计
     *
     * @param index
     * @param type
     * @param field
     * @return
     */
    public long countEmpty(String index, String type, String field) {
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.missingQuery(field))
                .should(QueryBuilders.termQuery(field + ".raw", ""));

        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                .setQuery(searchQuery).execute().actionGet();

        return (int) searchResponse.getHits().getTotalHits();
    }

    public long countTotalValues(String index, String type, String field) {
        final String COUNT_AGGR_NAME = "countValueTotal";
        QueryBuilder searchQuery = QueryBuilders.matchAllQuery();
        String aggField = stringMapping(index, type, field) ? field + ".raw" : field;
        ValueCountBuilder aggregation = AggregationBuilders.count(COUNT_AGGR_NAME).field(aggField);
        SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type)
                .setQuery(searchQuery).setNoFields().addAggregation(aggregation);
        SearchResponse searchResponse = srb.execute().actionGet();

        ValueCount valueCountAggr = searchResponse.getAggregations().get(COUNT_AGGR_NAME);
        return valueCountAggr.getValue();
    }

    public long countDistinct(String index, String type, String field) {
        final String DISTINCT_AGGR_NAME = "distinctTotal";
        QueryBuilder searchQuery = QueryBuilders.matchAllQuery();
        String aggField = stringMapping(index, type, field) ? field + ".raw" : field;
        CardinalityBuilder aggregation = AggregationBuilders.cardinality(DISTINCT_AGGR_NAME)
                .field(aggField).precisionThreshold(10000);
        SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type)
                .setQuery(searchQuery).setNoFields().addAggregation(aggregation);
        SearchResponse searchResponse = srb.execute().actionGet();

        Cardinality distinctAggr = searchResponse.getAggregations().get(DISTINCT_AGGR_NAME);
        return distinctAggr.getValue();
    }

    public List<Terms.Bucket> topValues(String index, String field, int top) {
        return topValues(index, null, field, top);
    }

    public List<Terms.Bucket> topValues(String index, String type, String field, int top) {
        final String TOP_AGGR_NAME = "topTerms";
        QueryBuilder searchQuery = QueryBuilders.matchAllQuery();
        String aggField = stringMapping(index, field) ? field + ".raw" : field;
        SearchRequestBuilder srb = client.prepareSearch(index)
                .setQuery(searchQuery).setNoFields()
                .addAggregation(AggregationBuilders.terms(TOP_AGGR_NAME).field(aggField).size(top));
        if (type != null && !type.isEmpty()) {
            srb.setTypes(type);
        }
        SearchResponse searchResponse = srb.execute().actionGet();

        Aggregation topAggr = searchResponse.getAggregations().get(TOP_AGGR_NAME);
        List<Terms.Bucket> buckets = ((InternalTerms) topAggr).getBuckets();
        return buckets;
    }

    public boolean createMapping(RSSchema schema, String index, String type) {
        XContentBuilder mappingBuilder = mapping(schema, type);
        PutMappingRequestBuilder builder = client.admin().indices().preparePutMapping(index);
        builder.setType(type).setSource(mappingBuilder).setMasterNodeTimeout(TimeValue.timeValueMinutes(1)).execute().actionGet();
        return true;
    }

    public boolean indexExist(String index) {
        IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
        if (response == null || !response.isExists()) {
            return false;
        }
        return true;
    }

    public boolean mappingExist(String indexname, String type) {
        ClusterStateResponse csr = client.admin().cluster().prepareState().setMasterNodeTimeout(TimeValue.timeValueMinutes(1)).execute().actionGet();
        ImmutableOpenMap<String, MappingMetaData> allMappings = csr.getState().getMetaData().index(indexname)
                .getMappings();

        return allMappings.containsKey(type);
    }

    public boolean mappingExist(String indexname, String type, String field)
            throws IOException {
        ClusterStateResponse csr = client.admin().cluster().prepareState().setMasterNodeTimeout(TimeValue.timeValueMinutes(1)).execute().actionGet();
        ImmutableOpenMap<String, MappingMetaData> allMappings = csr.getState().getMetaData().index(indexname)
                .getMappings();

        if (allMappings.containsKey(type)
                && allMappings.get(type).getSourceAsMap().get("properties").toString().contains(field))
            return true;
        return false;

    }

    public boolean stringMapping(String indexname, String type, String field) {
        GetMappingsResponse response = client.admin().indices().prepareGetMappings(indexname).setTypes(type).get();
        Map<String, Object> mappingMap = null;
        try {
            mappingMap = response.getMappings().get(indexname).get(type).getSourceAsMap();
        } catch (IOException e) {
            throw new ESMappingException(e.getMessage(), "es列对应异常。", e);
        }
        Map<String, Object> properties = (Map<String, Object>) mappingMap.get("properties");
        if (properties.containsKey(field)) {
            Map<String, Object> fieldMap = (Map<String, Object>) properties.get(field);
            return "string".equalsIgnoreCase(fieldMap.get("type").toString());

        }

        throw new ESException(String.format("\"%s\" does not exist in es mapping!", field),
                String.format("es对应关系中不存在\"%s\"。", field));
    }

    public boolean stringMapping(String indexname, String field) {
        GetMappingsResponse response = client.admin().indices().prepareGetMappings(indexname).get();
        try {
            ObjectContainer<MappingMetaData> typeMaps = response.getMappings().get(indexname).values();
            MappingMetaData[] tTypeMaps = typeMaps.toArray(MappingMetaData.class);
            for (MappingMetaData tTypeMap : tTypeMaps) {
                Map<String, Object> typeMap = tTypeMap.getSourceAsMap();
                Map<String, Object> properties = (Map<String, Object>) typeMap.get("properties");
                if (properties.containsKey(field)) {
                    Map<String, Object> fieldMap = (Map<String, Object>) properties.get(field);
                    return "string".equalsIgnoreCase(fieldMap.get("type").toString());
                }

                throw new ESException(String.format("\"%s\" does not exist in es mapping!", field),
                        String.format("es对应关系中不存在\"%s\"。", field));
            }
        } catch (IOException e) {
            throw new ESMappingException(e.getMessage(), "es对应关系异常。", e);
        }

        return false;
    }


    public XContentBuilder mapping(RSSchema schema, String type) {
        final int TERM_LIMIT = 200;
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.startObject(type);
            builder.startObject("properties");
            List<RSField> fields = schema.getFields();
            for (RSField field : fields) {
                String fieldStr = field.getName();
                builder.field(fieldStr);
                BaseType baseType = field.getBaseType();
                XContentBuilder fieldBuilder = TypeMapping.Type.getAllMapping().get(baseType).mapping();
                XContentParser parser = JsonXContent.jsonXContent.createParser(fieldBuilder.string());
                builder.copyCurrentStructure(parser);
            }
            builder.startObject("info");
            builder.field("type", "string");
            builder.field("index", "not_analyzed");
            builder.field("ignore_above", TERM_LIMIT);
            builder.endObject();
            builder.endObject();
            builder.endObject();
            builder.endObject();
            return builder;
        } catch (IOException e) {
            throw new ESMappingException(e.getMessage(), "es对应关系异常。", e);
        }
    }

    public boolean deleteIndex(String index) {
        if (indexExist(index)) {
            IndicesAdminClient adminClient = client.admin().indices();
            DeleteIndexResponse delete = adminClient.delete(new DeleteIndexRequest(index)).actionGet();
            return delete.isAcknowledged();
        }

        return true;
    }

    public void refresh(String index) {
        //client.admin().indices().prepareRefresh(index).get();
        //client.admin().indices().prepareRefresh(index).execute().actionGet();
        client.admin().indices().refresh(new RefreshRequest(index)).actionGet();

    }

    public void refresh() {
        client.admin().indices().prepareRefresh().get();
    }

    public boolean waitForHealth(String index) {
        return waitForHealth(index, 2000);
    }

    public boolean waitForHealth(String index, long timeout) {
        if (!checkClusterStatus(timeout)) {
            return false;
        }

        if (!indexExist(index)) {
            return false;
        }

        ClusterHealthResponse response = client.admin().cluster().prepareHealth(index)
                .setWaitForYellowStatus()
                .setTimeout(TimeValue.timeValueMillis(timeout))
                .get();

        ClusterHealthStatus status = response.getIndices().get(index).getStatus();
        if (status.equals(ClusterHealthStatus.RED)) {
            return false;
        }

        return true;
    }

    public boolean checkClusterStatus(long timeout) {
        final int times = 8;
        int count = 0;
        long waitTime = timeout;
        while (count <= times) {
            try {
                if (internalCheckClusterStatus(timeout)) {
                    return true;
                }
            } catch (NoNodeAvailableException e) {
                logger.warn(e.getMessage(), e);
                int exp = 1;
                for (int i = 0; i < count; i++) {
                    exp *= 2;
                }

                waitTime *= exp;
                count++;
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e1) {
                    logger.warn(e1.getMessage(), e1);
                }
                logger.info(String.format("Have tried for %d times", count));
            }
        }

        return false;
    }

    private boolean internalCheckClusterStatus(long timeout) {
        // Check cluster health
        ClusterHealthResponse response = client.admin().cluster().prepareHealth()
                .setWaitForYellowStatus()
                .setTimeout(TimeValue.timeValueMillis(timeout))
                .get();

        if (response.getStatus().equals(ClusterHealthStatus.RED)) {
            return false;
        }

        return true;
    }


    public ESConfig getEsConfig() {
        return esConfig;
    }

    public void setEsConfig(ESConfig esConfig) {
        this.esConfig = esConfig;
    }

    public void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }


}
