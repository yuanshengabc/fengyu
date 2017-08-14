package cn.deepclue.datamaster.streamer.config.elasticsearch;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by magneto on 16-6-14.
 */
public class BulkIndex {
    private static Logger logger = LoggerFactory.getLogger(BulkIndex.class);

    private static final int INDEX_BULK_SIZE = 20; // MB
    private static final int CONCURRENT_LEVEL = 8;
    private BulkProcessor bulkProcessor;

    public void buildBulk(Client client) {
        buildBulk(client, CONCURRENT_LEVEL);
    }

    public void buildBulk(Client client, int concurrentLevel) {
        buildBulk(client, concurrentLevel, INDEX_BULK_SIZE);
    }

    public void buildBulk(Client client, int concurrentLevel, int size) {
        bulkProcessor = BulkProcessor.builder(client, new CustomBulkListener())
                .setBulkSize(new ByteSizeValue(size, ByteSizeUnit.MB))
                .setConcurrentRequests(concurrentLevel).setBulkActions(-1).build();
    }

    public void endBulk() {
        if (bulkProcessor != null) {
            bulkProcessor.close();
        }
    }

    public void addBulkRequest(String indexName, String type, String id, Map<String, Object> prop) {
        bulkProcessor.add(new IndexRequest(indexName, type, id).source(prop));
    }

    public void addBulkRequest(String indexName, String type, Map<String, Object> prop) {
        bulkProcessor.add(new IndexRequest(indexName, type).source(prop));
    }

    public void addBulkRequest(UpdateRequest request) {
        bulkProcessor.add(request);
    }

    public void addBulkRequest(IndexRequest request) {
        bulkProcessor.add(request);
    }

    public void addBulkRequest(DeleteRequest request) {
        bulkProcessor.add(request);
    }

    class CustomBulkListener implements BulkProcessor.Listener {

        @Override public void beforeBulk(long executionId, BulkRequest request) {
            logger.info(String.format("start to bulk with %d requests", request.numberOfActions()));
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
            if (response.hasFailures()) {
                BulkItemResponse[] items = response.getItems();
                for (BulkItemResponse item: items) {
                    if (!item.isFailed()) {
                        continue;
                    }

                    logger.error(String.format("index:%s,type:%s,id:%s,failed:%s",
                        item.getIndex(), item.getType(), item.getId(), item.getFailureMessage()));
                }
            } else {
                logger.info("index count " + request.numberOfActions());
            }
        }

        @Override public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
            logger.error("index exceptions : " +  failure);
            StringBuilder exceptionData = new StringBuilder();
            exceptionData.append("exception data :");
            for(ActionRequest<?> ar : request.requests()) {
                IndexRequest ir = (IndexRequest)ar;
                exceptionData.append("index=").append(ir.index()).append(",type=").append(ir.type()).append(",id=").append(ir.id()).append("\n");
            }

            logger.error(exceptionData.toString());

            if (failure instanceof NoNodeAvailableException) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    logger.warn("{}", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
