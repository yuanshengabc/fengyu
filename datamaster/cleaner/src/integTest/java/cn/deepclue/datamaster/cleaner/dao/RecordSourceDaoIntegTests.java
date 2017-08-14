package cn.deepclue.datamaster.cleaner.dao;

import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;

/**
 * Created by xuzb on 08/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RecordSourceDaoIntegTests {

    @Autowired
    private RecordSourceDao recordSourceDao;

    @Test
    public void saveRSSchema() {
        RSSchema rsSchema = new RSSchema();

        RSField field = new RSField();
        field.setBaseType(BaseType.INT);
        field.setName("f1");

        rsSchema.addField(field);

        field = new RSField();
        field.setBaseType(BaseType.LONG);
        field.setName("f2");

        rsSchema.addField(field);

        recordSourceDao.saveRSSchema(1, rsSchema);

        RSSchema newSchema = recordSourceDao.getRSSchema(1);

        assertEquals(newSchema.count(), rsSchema.count());
    }
}
