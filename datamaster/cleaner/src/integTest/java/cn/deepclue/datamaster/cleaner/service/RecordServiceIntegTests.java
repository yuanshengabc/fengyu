package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordStatsRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.RecordsReqVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.RecordService;
import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.io.writer.ESWriter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by magneto on 17-4-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RecordServiceIntegTests {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private RecordService recordService;

    @Autowired
    private CleanerConfigurationProperties properties;

    @Before public void setUp() {
        ESConfig esConfig = properties.getEsconfig();
        String indexStr = "ds_1_rs_1";
        ESTypeConfig esTypeConfig = new ESTypeConfig(esConfig, indexStr, indexStr);

        ESWriter esWriter = new ESWriter(esTypeConfig);
        esWriter.open();
        esWriter.getSession().deleteIndex(indexStr);
        RSSchema schema = new RSSchema();
        RSField field1 = new RSField("field1", BaseType.TEXT);
        RSField field2 = new RSField("field2", BaseType.INT);
        RSField field3 = new RSField("field3", BaseType.DATE);
        schema.addField(field1);
        schema.addField(field2);
        schema.addField(field3);
        esWriter.writeSchema(schema);
        List<Record> records = new ArrayList<>();
        Record record1 = new Record(schema);
        record1.addValue("张忍");
        record1.addValue(1);
        record1.addValue(new Date());
        Record record2 = new Record(schema);
        record2.addValue("许振波");
        record2.addValue(2);
        record2.addValue(new Date());
        Record record3 = new Record(schema);
        record3.addValue("谢喆");
        record3.addValue(3);
        record3.addValue(new Date());
        records.add(record1);
        records.add(record2);
        records.add(record3);
        for (Record record: records) {
            esWriter.writeRecord(record);
        }
        esWriter.close();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test public void getRecords() {
        RecordsReqVO recordsReqVO = new RecordsReqVO();
        recordsReqVO.setRsid(1);
        recordsReqVO.setPage(0);
        recordsReqVO.setPageSize(10);
        recordsReqVO.setOrderBy("field1");
        recordsReqVO.setFilters(null);
        RecordList records = recordService.getRecords(recordsReqVO);
        assertThat(records.getRecords().size()).isEqualTo(3);
    }

    @Test public void getTopValues() {
        List<TopValueVO> topValueVOs = recordService.getTopValues(1, "field1");
        assertThat(topValueVOs.size()).isEqualTo(3);
    }

    @Test public void stats() {
        RecordStatsRespVO recordStatsRespVO = recordService.stats(1, "field1");
        assertThat(recordStatsRespVO.getTotal()).isEqualTo(3);
        assertThat(recordStatsRespVO.getDistinct()).isEqualTo(3);
        assertThat(recordStatsRespVO.getEmpty()).isEqualTo(0);
        assertThat(recordStatsRespVO.getNonempty()).isEqualTo(3);
    }
}
