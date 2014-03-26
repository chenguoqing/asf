package com.baidu.model;

import com.baidu.asf.model.Node;
import com.baidu.asf.model.xml.XMLDefinition;
import junit.framework.Assert;
import org.junit.Test;

/**
 * {@link com.baidu.asf.model.xml.XMLDefinition} test
 */
public class XMLDefinitionTest {
    @Test
    public void testXMLDefinition() throws Exception {
        XMLDefinition definition = new XMLDefinition("/model/asf1.xml");
        definition.build();

        Node node = definition.findNode("developerSubProcess/DeveloperTask");
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getFullId(),"developerSubProcess/DeveloperTaskDeveloperTask");
        System.out.println(node.getFullId());
    }
}
