package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.io.writer.ESWriter;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.base.ReadAndWriteBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/11.
 */
@Configuration
public class ESWriterTest extends ReadAndWriteBase {

    private String index;
    private String type;

    public ESWriterTest(){

        this.index=MatrixPrint.getESIndex(MatrixPrint.ES_INDEX_HEAD);
        this.type=MatrixPrint.getESType(MatrixPrint.ES_TYPE_HEAD);
    }

    public void testStart(){

        ESTypeConfig esTypeConfig=ioProperties.getEsTypeConfig();
        esTypeConfig.setIndex(index);
        esTypeConfig.setType(type);

        ESWriter esWriter=new ESWriter(esTypeConfig);

        MatrixPrint.printTestStart("ESWriter:");
        MatrixPrint.print("    ES index:  "+index+"  ,type:  "+type);
        testWriter(esWriter);
    }
}