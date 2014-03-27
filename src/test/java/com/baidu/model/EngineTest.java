package com.baidu.model;

import com.baidu.asf.engine.ASFInstance;
import com.baidu.asf.engine.ExecutionTask;
import com.baidu.asf.engine.spring.ASFEngineProxy;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;


/**
 * Created by chenguoqing01 on 2014/3/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf.xml")
public class EngineTest {

    @Autowired
    private ASFEngineProxy engineProxy;
    @Autowired
    private DataSource dataSource;

    @Test
    public void testStartASFInstance() {
        engineProxy.startASFInstance();
//        SpringJdbcEntityManager entityManager = new SpringJdbcEntityManager(dataSource);
//        VariableEntity entity = new VariableEntity();
//        entity.setInstanceId(55);
//        entity.setName("name");
//        entity.setString("zs");
//        entity.setType(VariableEntity.VariableType.STRING);
//        entity.setVariableClass(VariableEntity.VariableClass.SYSTEM);
//        entityManager.createVariable(entity);
//        entityManager.createVariable(entity);
    }

    @Test
    public void testComplete() {
        ASFInstance instance = engineProxy.startASFInstance();
        Assert.assertNotNull(instance);

        // current is DeveloperTask
        List<ExecutionTask> tasks = instance.getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(tasks.size(), 1);

        // goto test task
        ExecutionTask task = tasks.iterator().next();
        task.complete();

        // current is DeveloperReady
        tasks = instance.getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(tasks.size(), 1);

        // goto sub end
        task = tasks.iterator().next();
        task.complete();
    }
}
