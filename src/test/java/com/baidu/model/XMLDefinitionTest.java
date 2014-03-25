package com.baidu.model;

import com.baidu.asf.model.xml.XMLDefinition;
import org.junit.Test;

/**
 * {@link com.baidu.asf.model.xml.XMLDefinition} test
 */
public class XMLDefinitionTest {
    @Test
    public void testXMLDefinition() throws Exception {
        XMLDefinition definition = new XMLDefinition("/model/asf1.xml");
        definition.build();
    }
}
