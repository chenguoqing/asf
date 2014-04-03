package com.baidu.asf.engine;

import com.baidu.asf.engine.spring.ASFEngineProxy;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf2.xml")
public class InclusiveGatewayTest {

    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    public void testInclusiveGateway() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("selfTestPass", true);
        variables.put("opTestPass", true);

        ASFInstance instance = engineProxy.startASFInstance(variables);
        Assert.assertNotNull(instance);

        List<ExecutionTask> tasks = instance.getTasks();

        Assert.assertNotNull(tasks);
        Assert.assertEquals(tasks.size(), 2);

        for (ExecutionTask task : tasks) {
            task.complete(variables);
        }

        tasks = instance.getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(tasks.size(), 2);

        for (ExecutionTask task : tasks) {
            Map<String, Object> _variables = new HashMap<String, Object>();
            if (task.getUserTask().getId().equals("qaTest")) {
                _variables.put("qaTestPass", true);
                task.complete(_variables);
            } else if (task.getUserTask().getId().equals("scmTest")) {
                _variables.put("scmTestPass", true);
                task.complete(_variables);
            }
        }
    }
}
