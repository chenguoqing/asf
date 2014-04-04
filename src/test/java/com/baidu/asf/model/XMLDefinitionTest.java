package com.baidu.asf.model;

import com.baidu.asf.model.xml.XMLDefinition;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * {@link com.baidu.asf.model.xml.XMLDefinition} test
 */
public class XMLDefinitionTest {
    @Test
    public void testXMLDefinition() throws Exception {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("/model/asf1.xml");
        XMLDefinition definition = new XMLDefinition("/model/asf1.xml", resource.getInputStream());

        Node node = definition.findNode("developerSubProcess/DeveloperTask");
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getFullId(), "developerSubProcess/DeveloperTask");
        System.out.println(node.getFullId());
    }
}
