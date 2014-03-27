package com.baidu.asf.model.xml;

import com.baidu.asf.engine.ExecutionListener;
import com.baidu.asf.expression.JEXLConditionExpression;
import com.baidu.asf.model.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * XML implementation
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
    public static final String ELEMENT_FLOW = "sequenceFlow";
    public static final String ELEMENT_CONDITION = "conditionExpression";
    public static final String ELEMENT_SUBPROCESS = "subProcess";

    public static final String ATTR_ID = "id";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_DESCRIPTION = "description";
    public static final String ATTR_VERSION = "version";
    public static final String ATTR_LISTENER = "listener";
    public static final String ATTR_CLASS = "class";
    public static final String ATTR_SOURCE_REF = "sourceRef";
    public static final String ATTR_TARGET_REF = "targetRef";
    public static final String ATTR_EXPRESSION = "expression";

    private final String resourcePath;

    private Stack<StackEntry> definitionStack = new Stack<StackEntry>();

    public XMLDefinition(String resourcePath) {
        if (resourcePath == null) {
            throw new IllegalArgumentException();
        }
        this.resourcePath = resourcePath;
    }

    private XMLDefinition(XMLDefinition parent) {
        this(parent.resourcePath);
        setParent(parent);
    }

    @Override
    public void build() {

        InputStream inputStream;
        try {
            if (resourcePath.startsWith(PREFIX_CLASSPATH)) {
                inputStream = loadResource(resourcePath.substring(0, PREFIX_CLASSPATH.length()));
            } else if (resourcePath.startsWith(PREFIX_FILE)) {
                inputStream = new FileInputStream(resourcePath.substring(0, PREFIX_FILE.length()));
            } else {
                inputStream = loadResource(resourcePath);
            }
        } catch (IOException e) {
            throw new ASFModelException(e);
        }
        if (inputStream == null) {
            throw new ASFModelException("Not found the xml resource for path:" + resourcePath);
        }

        // parse xml stream to model
        parseModel(inputStream);

        // build and validate model
        buildDefinition();
    }

    /**
     * Parse the xml stream to XMLDefinition instance
     */
    public void parseModel(InputStream inputStream) {

        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputStream);
            Element root = document.getRootElement();

            definitionStack.push(new StackEntry(this, null));
            parseDefinition(root);
            parse(root);
        } catch (Exception e) {
            throw new ASFModelParserException(e);
        }
    }

    /**
     * Parse xml element
     */
    private void parse(Element e) {
        for (Object o : e.elements()) {
            Element element = (Element) o;
            String name = element.getName();
            Node node;
            if (name.equals(ELEMENT_STARTEVENT)) {
                node = parseStartEvent(element);
            } else if (name.equals(ELEMENT_ENDEVENT)) {
                node = parseEndEvent(element);
            } else if (name.equals(ELEMENT_USER_TASK)) {
                node = parseUserTask(element);
            } else if (name.equals(ELEMENT_SERVICE_TASK)) {
                node = parseServiceTask(element);
            } else if (name.equals(ELEMENT_PARALLEL_GATEWAY)) {
                node = parseParallelGateway(element);
            } else if (name.equals(ELEMENT_EXCLUSIVE_GATEWAY)) {
                node = parseExclusiveGateway(element);
            } else if (name.equals(ELEMENT_INCLUSIVE_GATEWAY)) {
                node = parseInclusiveGateway(element);
            } else if (name.equals(ELEMENT_FLOW)) {
                Flow flow = parseFlow(element);
                definitionStack.peek().definition.addFLow(flow);
                continue;
            } else if (name.equals(ELEMENT_SUBPROCESS)) {
                node = parseSubprocess(element);
            } else {
                // ignore invalidate tags
                continue;
            }

            // parseModel common elements
            parseCommonNode(element, node);
        }
    }

    private void parseDefinition(Element root) {
        definitionStack.peek().definition.setId(root.attributeValue(ATTR_ID));
        definitionStack.peek().definition.setDescription(root.attributeValue(ATTR_DESCRIPTION));
        setVersion(Integer.parseInt(root.attributeValue(ATTR_VERSION)));
    }

    private void parseCommonNode(Element element, Node node) {
        node.setId(element.attributeValue(ATTR_ID));
        node.setName(element.attributeValue(ATTR_NAME));
        node.setDescription(element.attributeValue(ATTR_DESCRIPTION));
        parseListener(element, node);
        definitionStack.peek().definition.addNode(node);
    }

    private void parseListener(Element element, Node node) {
        Element listenerElement = element.element(ATTR_LISTENER);

        if (listenerElement != null) {
            String className = listenerElement.attributeValue(ATTR_CLASS);
            Class<?> listenerClass;
            try {
                listenerClass = loadClass(className);
            } catch (Exception e) {
                throw new ASFModelException("Failed to load listener class:" + className, e);
            }

            if (!(ExecutionListener.class.isAssignableFrom(listenerClass))) {
                throw new ASFModelException("Invalidate listener class:" + className);
            }

            try {
                ExecutionListener listener = (ExecutionListener) listenerClass.newInstance();
                node.addExecutionListener(listener);
            } catch (Exception e) {
                throw new ASFModelException("Failed to create listener instance for " + className, e);
            }
        }
    }

    private StartEvent parseStartEvent(Element element) {
        return new StartEvent(definitionStack.peek().node);
    }

    private EndEvent parseEndEvent(Element element) {
        return new EndEvent(definitionStack.peek().node);
    }

    private UserTask parseUserTask(Element element) {
        return new UserTask(definitionStack.peek().node);
    }

    private ServiceTask parseServiceTask(Element element) {
        return new ServiceTask(definitionStack.peek().node);
    }

    private ParallelGateway parseParallelGateway(Element element) {
        return new ParallelGateway(definitionStack.peek().node);
    }

    private ExclusiveGateway parseExclusiveGateway(Element element) {
        return new ExclusiveGateway(definitionStack.peek().node);
    }

    private InclusiveGateway parseInclusiveGateway(Element element) {
        return new InclusiveGateway(definitionStack.peek().node);
    }

    private Flow parseFlow(Element element) {
        String sourceRef = element.attributeValue(ATTR_SOURCE_REF);
        String targetRef = element.attributeValue(ATTR_TARGET_REF);

        JEXLConditionExpression jexlExpression = null;
        Element condElement = element.element(ELEMENT_CONDITION);
        if (condElement != null) {
            String expression = condElement.attributeValue(ATTR_EXPRESSION);
            jexlExpression = new JEXLConditionExpression();
            jexlExpression.setExpression(expression);
        }
        return new SequenceFlow(sourceRef, targetRef, false, jexlExpression);
    }

    private SubProcess parseSubprocess(Element element) {
        DefaultSubProcess subProcess = new DefaultSubProcess(definitionStack.peek().node);

        final XMLDefinition parent = definitionStack.peek().definition;
        XMLDefinition subDefinition = new XMLDefinition(parent);
        parent.addSubDefinition(element.attributeValue(ATTR_ID), subDefinition);

        definitionStack.push(new StackEntry(subDefinition, subProcess));
        parse(element);
        definitionStack.pop();
        subProcess.setSubProcessDefinition(subDefinition);
        return subProcess;
    }

    /**
     * Load resource from classpath
     */
    private InputStream loadResource(String path) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            inputStream = XMLDefinition.class.getResourceAsStream(path);
        }

        return inputStream;
    }

    private Class<?> loadClass(String name) throws Exception {
        Class<?> cz = Thread.currentThread().getContextClassLoader().loadClass(name);
        if (cz == null) {
            cz = XMLDefinition.class.getClassLoader().loadClass(name);
        }

        return cz;
    }

    static class StackEntry {

        final XMLDefinition definition;
        final SubProcess node;

        StackEntry(XMLDefinition definition, SubProcess node) {
            this.definition = definition;
            this.node = node;
        }
    }
}
