package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.config.elasticsearch.BulkIndex;
import cn.deepclue.datamaster.streamer.exception.ESException;
import cn.deepclue.datamaster.streamer.io.RecordConverter;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import cn.deepclue.datamaster.streamer.session.ESSession;

/**
 * Created by magneto on 17-4-6.
 */
public class ESWriter implements Writer {
    private ESTypeConfig esConfig;

    private ESSession session;
    private BulkIndex bulkIndex;

    public ESWriter(ESTypeConfig esConfig) {
        this.esConfig = esConfig;
    }

    @Override public void writeSchema(RSSchema schema) {
        schema = SchemaConverter.toESSchema(schema);
        synchronized (ESWriter.class) {
            if (!session.indexExist(esConfig.getIndex())) {
                session.createIndex(esConfig.getIndex());
            }
            if (!session.mappingExist(esConfig.getIndex(), esConfig.getType())) {
                session.createMapping(schema, esConfig.getIndex(), esConfig.getType());
            }

            if (!session.waitForHealth(esConfig.getIndex())) {
                throw new ESException(String.format("index: %s in bad status!", esConfig.getIndex()),
                        String.format("索引：%s处在非健康状态！",esConfig.getIndex()));
            }
        }
    }

    @Override public void writeRecord(Record record) {
        Record esRecord = RecordConverter.toESRecord(record);
        bulkIndex.addBulkRequest(esConfig.getIndex(), esConfig.getType(), esRecord.getKey(), esRecord.getValueMap());
    }

    @Override public void open() {
        this.session = new ESSession(esConfig);

        if (!session.checkClusterStatus(2000)) {
            throw new ESException(String.format("index: %s in bad status!", esConfig.getIndex()),
                    String.format("索引：%s处在非健康状态！",esConfig.getIndex()));
        }

        this.bulkIndex = new BulkIndex();
        if (esConfig.getParallelism() != 0) {
            bulkIndex.buildBulk(session.getClient(), esConfig.getParallelism());
        } else {
            bulkIndex.buildBulk(session.getClient());
        }
    }

    @Override public void close() {
        stopBulk();
        if (session != null) {
            session.close();
        }
    }

    public void stopBulk() {
        if (bulkIndex != null) {
            bulkIndex.endBulk();
        }
        if (session != null) {
            session.refresh(esConfig.getIndex());
        }
    }

    public ESSession getSession() {
        return session;
    }
}
