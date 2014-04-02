package com.baidu.asf.engine;

import com.baidu.asf.engine.spring.ASFEngineProxy;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf2.xml")
public class InclusiveGatewayTest {

    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    public void testInclusiveGateway() {
        ASFInstance instance = engineProxy.startASFInstance();
        Assert.assertNotNull(instance);
    }
}
