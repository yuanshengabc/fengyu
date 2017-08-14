package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;
import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import cn.deepclue.datamaster.streamer.session.ESFilterBuilder;
import cn.deepclue.datamaster.streamer.session.ESSession;
import cn.deepclue.datamaster.streamer.session.esfilter.ESFilter;
import cn.deepclue.datamaster.streamer.transform.filter.Filter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by magneto on 17-4-5.
 */
@Lazy
@Repository("recordDao")
public class RecordImpl implements RecordDao {
    private static int top = 1000;

    @Autowired
    private CleanerConfigurationProperties properties;

    private ESSession esSession;

    DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

    @PostConstruct
    private void init() {
        esSession = new ESSession(properties.getEsconfig());
    }

    @PreDestroy
    private void destory() {
        if (esSession != null) {
            esSession.close();
        }
    }

    @Override
    public RecordList getRecords(RecordSource recordSource, RSSchema rsSchema,
                                 int page, int pageSize, String orderBy,
                                 boolean asc, List<Filter> filters) {

        QueryBuilder query = QueryBuilders.matchAllQuery();
        if (filters != null && !filters.isEmpty()) {
            //获取filters时，会转换列名
            List<ESFilter> esFilters = ESFilterBuilder.build(filters);
            if (esFilters != null && !esFilters.isEmpty()) {
                BoolQueryBuilder filterQuery = QueryBuilders.boolQuery();
                for (ESFilter esFilter : esFilters) {
                    QueryBuilder filter = esFilter.filter();
                    filterQuery.must(filter);
                }
                query = filterQuery;
            }
        }

        String index = recordSource.getESIndexName();
        String type = recordSource.getESTypeName();
        SearchHits hits = esSession.allDocs(index, type, query, page, pageSize, orderBy, asc);
        List<Record> records = new ArrayList<>();
        for (SearchHit hit : hits) {
            Record record = toRecord(hit, rsSchema);
            records.add(record);
        }

        RecordList recordList = new RecordList();
        recordList.setRecords(records);
        recordList.setTotal(hits.getTotalHits());

        return recordList;
    }

    @Override
    public List<TopValueVO> getTopValues(RecordSource recordSource, String fieldName) {
        fieldName = SchemaConverter.toESSchemaField(fieldName);

        String index = recordSource.getESIndexName();
        String type = recordSource.getESTypeName();
        List<TopValueVO> topValueVOs = new ArrayList<>();
        List<Terms.Bucket> buckets = esSession.topValues(index, type, fieldName, top);
        for (Terms.Bucket bucket : buckets) {
            TopValueVO topValueVO = new TopValueVO(bucket.getKeyAsString(), (int) bucket.getDocCount());
            topValueVOs.add(topValueVO);
        }

        if (topValueVOs.isEmpty()) {
            TopValueVO topValueVO = new TopValueVO(null, (int) esSession.totalDocs(index, type));
            topValueVOs.add(topValueVO);
        }

        return topValueVOs;
    }

    @Override
    public int countTotal(RecordSource recordSource) {

        String index = recordSource.getESIndexName();
        String type = recordSource.getESTypeName();
        return (int) esSession.totalDocs(index, type);
    }

    @Override
    public int countEmpty(RecordSource recordSource, String fieldName) {
        fieldName = SchemaConverter.toESSchemaField(fieldName);

        String index = recordSource.getESIndexName();
        String type = recordSource.getESTypeName();

        return (int) esSession.countEmpty(index, type, fieldName);
    }

    @Override
    public int countDistinct(RecordSource recordSource, String fieldName) {
        fieldName = SchemaConverter.toESSchemaField(fieldName);

        String index = recordSource.getESIndexName();
        String type = recordSource.getESTypeName();

        return (int) esSession.countDistinct(index, type, fieldName);
    }

    @Override
    public boolean deleteIndex(String indexName) {
        return esSession.deleteIndex(indexName);
    }

    public Record toRecord(SearchHit hit, RSSchema rsSchema) {
        Record record = new Record(rsSchema);

        record.setKey(hit.getId());
        Map<String, Object> doc = hit.getSource();
        for (RSField rsField : rsSchema.getFields()) {
            String fieldName = rsField.getName();
            //查询值时，使用转换后的列名
            fieldName = SchemaConverter.toESSchemaField(fieldName);
            Object value = doc.get(fieldName);

            if (value != null && rsField.getBaseType() == BaseType.DATE) {
                if (value instanceof Long) {
                    record.addValue(new Date((Long) value));
                } else if (value instanceof String) {
                    record.addValue(dateTimeFormatter.parseDateTime((String) value).toDate());
                }
            } else {
                record.addValue(value);
            }
        }

        return record;
    }
}
