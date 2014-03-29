package com.baidu.model;

import org.apache.commons.digester3.Digester;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by chenguoqing01 on 14-3-28.
 */
public class DigesterModelParser {

    @Test
    public void testParser() throws Exception {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("process", TestDefinition.class);
        digester.addBeanPropertySetter("process");


        InputStream in = DigesterModelParser.class.getResourceAsStream("/model/asf1.xml");
        digester.parse(in);


    }
}
