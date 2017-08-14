package cn.deepclue.datamaster.streamer.session.esfilter;

import cn.deepclue.datamaster.streamer.io.SchemaConverter;

/**
 * Created by magneto on 17-4-13.
 */
abstract public class ESSingleFieldFilter implements ESFilter {
    public ESSingleFieldFilter(String field) {
        //分析时已转换列名，所以筛选时也需要转换列名
        this.field = SchemaConverter.toESSchemaField(field);
    }

    protected String field;

}
