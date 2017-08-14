package cn.deepclue.datamaster.testio.base;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.Writer;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.ioConfig.IOProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Random;

/**
 * Created by lilei-mac on 2017/4/23.
 */
@Configuration
@EnableConfigurationProperties(IOProperties.class)
public class ReadAndWriteBase {

    @Autowired
    public IOProperties ioProperties;

    public void testWriter(Writer writer){

        writer.open();
        writer.writeSchema(getRsSchema());

        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(),ioProperties.getIoBean().getRecordColumn());
        long startTime=MatrixPrint.getCurrentTimeLong();
        for (int i = 0; i < ioProperties.getIoBean().getRecordNumber(); ++i) {
            writer.writeRecord(getRecord());
        }
        MatrixPrint.printResultTime(startTime);
        MatrixPrint.printReslut("Writer  ",ioProperties.getIoBean().getRecordNumber(),startTime);
        writer.close();
    }

    public void testReader(Reader reader){

        reader.open();
        reader.readSchema();

        MatrixPrint.printDetails(ioProperties.getIoBean().getRecordNumber(),ioProperties.getIoBean().getRecordColumn());
        long startTime=MatrixPrint.getCurrentTimeLong();
        while(reader.hasNext()){
            reader.readRecord();
        }
        MatrixPrint.printResultTime(startTime);
        MatrixPrint.printReslut("Reader  ",ioProperties.getIoBean().getRecordNumber(),startTime);
        reader.close();
    }

    private Record getRecord(){

        Random random = new Random();
        Record record = new Record(ioProperties.getRsSchema());
        for(int i=0;i<ioProperties.getIoBean().getRecordColumn()/6;i++){
            record.addValue("testContentString");
            record.addValue(random.nextInt());
            record.addValue(random.nextLong());
            record.addValue(random.nextFloat());
            record.addValue(random.nextDouble());
            record.addValue(new Date());
        }
        return record;
    }

    private RSSchema getRsSchema(){

        if(!ioProperties.getRsSchema().getFields().isEmpty()){
            return ioProperties.getRsSchema();
        }
        for(int i=0;i<ioProperties.getIoBean().getRecordColumn()/6;i++){
            addField(getFieldName(i,1), BaseType.TEXT);
            addField(getFieldName(i,2),BaseType.INT);
            addField(getFieldName(i,3),BaseType.LONG);
            addField(getFieldName(i,4),BaseType.FLOAT);
            addField(getFieldName(i,5),BaseType.DOUBLE);
            addField(getFieldName(i,6),BaseType.DATE);
        }
        return ioProperties.getRsSchema();
    }

    private void addField(String name,BaseType type){

        RSField field = new RSField();
        field.setBaseType(type);
        field.setName(name);
        ioProperties.getRsSchema().addField(field);
    }

    private String getFieldName(int i,int num){

        return  "f"+String.valueOf(6*i+num);
    }
}