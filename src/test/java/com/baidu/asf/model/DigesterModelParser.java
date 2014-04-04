package com.baidu.asf.model;

import com.baidu.asf.model.xml.XMLDefinition;
import junit.framework.Assert;
import org.apache.commons.digester3.Digester;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;

/**
 * Created by chenguoqing01 on 14-3-28.
 */
public class DigesterModelParser {

    @Test
    public void testParser() throws Exception {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("process", TestDefinition.class);
        digester.addSetProperties("process");


        InputStream in = DigesterModelParser.class.getResourceAsStream("/model/asf1.xml");
        digester.parse(in);
    }
}
