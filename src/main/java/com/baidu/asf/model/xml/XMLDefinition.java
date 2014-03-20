package com.baidu.asf.model.xml;

import com.baidu.asf.model.ASFModelParserException;
import com.baidu.asf.model.AbstractASFDefinition;
import com.baidu.asf.model.ActElement;
import com.baidu.asf.model.StartEvent;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * Created by chenguoqing01 on 14-3-20.
 */
public class XMLDefinition extends AbstractASFDefinition {

    public static final String PREFIX_CLASSPATH = "classpath:";
    public static final String PREFIX_FILE = "file:";

    public static final String ELEMENT_STARTEVENT = "startEvent";
    public static final String ELEMENT_ENDEVENT = "endEvent";
    public static final String ELEMENT_USER_TASK = "userTask";
    public static final String ELEMENT_SERVICE_TASK = "serviceTask";
    public static final String ELEMENT_PARALLEL_GATEWAY = "parallelGateway";
    public static final String ELEMENT_EXCLUSIVE_GATEWAY = "exclusiveGateway";
    public static final String ELEMENT_INCLUSIVE_GATEWAY = "inclusiveGateway";
    public static final String ELEMENT_FLOW = "flow";
    public static final String ELEMENT_SUBPROCESS = "subProcess";

    private final String resourcePath;

    private Stack<XMLDefinition> definitionStack = new Stack<XMLDefinition>();

    public XMLDefinition(String resourcePath) {
        if (resourcePath == null) {
            throw new IllegalArgumentException();
        }
        this.resourcePath = resourcePath;
    }

    @Override
    public void build() throws IOException {

        InputStream inputStream;
        if (resourcePath.startsWith(PREFIX_CLASSPATH)) {
            inputStream = loadResource(resourcePath.substring(0, PREFIX_CLASSPATH.length()));
        } else if (resourcePath.startsWith(PREFIX_FILE)) {
            inputStream = new FileInputStream(resourcePath.substring(0, PREFIX_FILE.length()));
        } else {
            inputStream = loadResource(resourcePath);
        }

        if (inputStream == null) {
            throw new IOException("Not found the xml resource for path:" + resourcePath);
        }

        parse(inputStream);
    }

    public void parse(InputStream inputStream) {

        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputStream);
            Element root = document.getRootElement();

            definitionStack.push(this);
            parseDefinition(root);
            parse(root);
        } catch (Exception e) {
            throw new ASFModelParserException(e);
        }
    }

    private void parse(Element e) {
        for (Object o : e.elements()) {
            Element element = (Element) o;
            String name = element.getName();
            ActElement actElement;
            if (name.equals(ELEMENT_STARTEVENT)) {
                actElement = parseStartEvent(element);

            } else if (name.equals(ELEMENT_ENDEVENT)) {
                actElement = parseEndEvent(element);
            } else if (name.equals(ELEMENT_USER_TASK)) {
                actElement = parseUserTask(element);
            } else if (name.equals(ELEMENT_SERVICE_TASK)) {
                actElement = parseServiceTask(element);
            } else if (name.equals(ELEMENT_PARALLEL_GATEWAY)) {
                actElement = parseParallelGateway(element);
            } else if (name.equals(ELEMENT_EXCLUSIVE_GATEWAY)) {
                actElement = parseExclusiveGateway(element);
            } else if (name.equals(ELEMENT_INCLUSIVE_GATEWAY)) {
                actElement = parseInclusiveGateway(element);
            } else if (name.equals(ELEMENT_FLOW)) {
                actElement = parseFlow(element);
            } else if (name.equals(ELEMENT_SUBPROCESS)) {
                actElement = parseSubprocess(element);
            } else {
                // ignore invalidate tags
                continue;
            }

            parseCommonElement(element, actElement);
        }
    }

    private void parseDefinition(Element root) {
        parseCommonElement(root, this);
        this.version = Integer.parseInt(root.attributeValue("version"));
    }

    private void parseCommonElement(Element element, ActElement actElement) {
        actElement.setId(element.attributeValue("id"));
        actElement.setName(element.attributeValue("name"));
        actElement.setDescription(element.attributeValue("description"));


    }

    private ActElement parseStartEvent(Element element) {
        return new StartEvent();
    }

    private ActElement parseEndEvent(Element element) {
        return null;
    }

    private ActElement parseUserTask(Element element) {
        return null;
    }

    private ActElement parseServiceTask(Element element) {
        return null;
    }

    private ActElement parseParallelGateway(Element element) {
        return null;
    }

    private ActElement parseExclusiveGateway(Element element) {
        return null;
    }

    private ActElement parseInclusiveGateway(Element element) {
        return null;
    }

    private ActElement parseFlow(Element element) {
        return null;
    }

    private ActElement parseSubprocess(Element element) {
        return null;
    }

    private InputStream loadResource(String path) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            inputStream = XMLDefinition.class.getResourceAsStream(path);
        }

        return inputStream;
    }
}
