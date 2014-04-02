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

import java.util.List;


/**
 * Test cases
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf.xml")
public class EngineTest {

    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    public void testStartASFInstance() {
        engineProxy.startASFInstance();
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
