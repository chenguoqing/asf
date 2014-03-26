package com.baidu.model;

import com.baidu.asf.engine.ASFInstance;
import com.baidu.asf.engine.spring.ASFEngineProxy;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf.xml")
public class VariableTest {
    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    public void testVariable() {
        ASFInstance instance = engineProxy.findASFInstance(30);
//        instance.setVariable("b", true);

        Boolean b = (Boolean) instance.getVariable("b");

        Assert.assertTrue(b);
    }
}
