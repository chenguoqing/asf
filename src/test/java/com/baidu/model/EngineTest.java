package com.baidu.model;

import com.baidu.asf.engine.spring.ASFEngineProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenguoqing01 on 2014/3/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf.xml")
public class EngineTest {

    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    @Transactional
    public void testEngine() {
        engineProxy.startASFInstance();
    }
}
