package com.baidu.asf.engine;

import com.baidu.asf.engine.spring.ASFEngineProxy;
import com.baidu.asf.model.Node;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Testing three level sub process
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf3.xml")
public class MultipleLevelSubProcessTest {

    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    public void test3LevelSubProcess() {
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

        Node node = instance.getDefinition().findNode("developerSubProcess/developingProcess/rdCoding");

        Assert.assertNotNull(node);
        Assert.assertNotNull(node.getDefinition());
        Assert.assertFalse(node.getDefinition() == instance.getDefinition());
    }
}
