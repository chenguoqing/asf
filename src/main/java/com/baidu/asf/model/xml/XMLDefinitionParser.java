package com.baidu.asf.model.xml;

import com.baidu.asf.model.ASFModelParserException;
import com.baidu.asf.model.AbstractASFDefinition;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * {@link XMLDefinitionParser} parses the xml file to {@link com.baidu.asf.model.AbstractASFDefinition}
 */
public class XMLDefinitionParser {

    public static final String ELEMENT_STARTEVENT = "startEvent";
    public static final String ELEMENT_ENDEVENT = "endEvent";

    public static AbstractASFDefinition parse(String file) {
        return null;
    }

    public static AbstractASFDefinition parse(File file) {
        return null;
    }

    public static AbstractASFDefinition parse(InputStream inputStream) throws IOException {
        return null;
    }

    public static AbstractASFDefinition parse(Reader reader) {

        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(reader);
            Element root = document.getRootElement();

//            AbstractASFDefinition definition = new AbstractASFDefinition();

            String defId = root.attributeValue("id");
            String defName = root.attributeValue("name");
            String version = root.attributeValue("version");

        } catch (Exception e) {
            throw new ASFModelParserException(e);
        }
        return null;
    }
}
