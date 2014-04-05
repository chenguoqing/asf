package com.baidu.asf.model;

import com.baidu.asf.engine.ASFEngineConfiguration;
import com.baidu.asf.model.xml.XMLDefinition;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * {@link com.baidu.asf.model.xml.XMLDefinition} test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf.xml")
public class XMLDefinitionTest {
    @Autowired
    private ASFEngineConfiguration configuration;

    @Test
    public void testXMLDefinition() throws Exception {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("/model/asf3.xml");
        XMLDefinition definition = new XMLDefinition(resource.getURL().getFile(), resource.getInputStream(), configuration);

        Node node = definition.findNode("developerSubProcess/DeveloperTask");
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getFullId(), "developerSubProcess/DeveloperTask");
        System.out.println(node.getFullId());
    }
}
