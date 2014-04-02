package com.baidu.asf.model.xml;

import com.baidu.asf.ASFException;
import com.baidu.asf.expression.JEXLConditionExpression;
import com.baidu.asf.model.*;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ObjectCreateRule;
import org.apache.commons.digester3.Rule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.validation.SchemaFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The xml configuration implementation of process definition
 */
public class XMLDefinition extends AbstractASFDefinition {

    private static final String schemaLanguage = "http://www.w3.org/2001/XMLSchema";
    private static final String SCHEMA_PATH = "/schema/asf.xsd";
    private static final String PREFIX_CLASSPATH = "classpath:";
    private static final String PREFIX_FILE = "file:";

    private static final String ELEMENT_PROCESS = "process";
    private static final String ELEMENT_START_EVENT = "startEvent";
    private static final String ELEMENT_END_EVENT = "endEvent";
    private static final String ELEMENT_USER_TASK = "userTask";
    private static final String ELEMENT_SERVICE_TASK = "serviceTask";
    private static final String ELEMENT_PARALLEL_GATEWAY = "parallelGateway";
    private static final String ELEMENT_EXCLUSIVE_GATEWAY = "exclusiveGateway";
    private static final String ELEMENT_INCLUSIVE_GATEWAY = "inclusiveGateway";
    private static final String ELEMENT_FLOW = "sequenceFlow";
    private static final String ELEMENT_CONDITION = "conditionExpression";
    private static final String ELEMENT_SUBPROCESS = "subProcess";
    private static final String ELEMENT_LISTENER = "listener";

    private final String resourcePath;
    private final boolean validate;

    /**
     * Constructor with validate
     */
    public XMLDefinition(String resourcePath) {
        this(resourcePath, true);
    }

    /**
     * Constructor with validate parameter
     */
    private XMLDefinition(String resourcePath, boolean validate) {
        if (resourcePath == null) {
            throw new IllegalArgumentException();
        }
        this.resourcePath = resourcePath;
        this.validate = validate;
    }

    /**
     * Constructor, only for sub definition
     */
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
        Digester digester = new Digester();

        digester.push(this);
        digester.addSetProperties(ELEMENT_PROCESS);

        addNodeRule(digester, ELEMENT_START_EVENT, StartEvent.class);
        addNodeRule(digester, ELEMENT_USER_TASK, UserTask.class);
        addSequenceFlowRule(digester);
        addNodeRule(digester, ELEMENT_SERVICE_TASK, ServiceTask.class);
        addNodeRule(digester, ELEMENT_PARALLEL_GATEWAY, ParallelGateway.class);
        addNodeRule(digester, ELEMENT_EXCLUSIVE_GATEWAY, ExclusiveGateway.class);
        addNodeRule(digester, ELEMENT_INCLUSIVE_GATEWAY, InclusiveGateway.class);
        addNodeRule(digester, ELEMENT_SUBPROCESS, DefaultSubProcess.class);
        addNodeRule(digester, ELEMENT_END_EVENT, EndEvent.class);

        try {
            if (validate) {
                SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);
                digester.setXMLSchema(schemaFactory.newSchema(XMLDefinition.class.getResource(SCHEMA_PATH)));
            }
            digester.parse(inputStream);
        } catch (IOException e) {
            throw new ASFException("Failed to load xml resource:" + resourcePath);
        } catch (SAXException e) {
            throw new ASFModelException("Failed to parse xml resource:" + resourcePath);
        }
    }

    /**
     * Encapsulate the digester rules for node type
     */
    private void addNodeRule(Digester digester, String nodeName, Class<? extends Node> nodeClass) {
        final String nodeRule = "*/" + nodeName;
        digester.addObjectCreate(nodeRule, nodeClass);
        digester.addSetProperties(nodeRule);

        // push the sub definition to stack and pop it when end element
        if (nodeClass == DefaultSubProcess.class) {
            digester.addRule(nodeRule, new SubProcessDefinitionCreateRule());
            digester.addSetNext(nodeRule, "addNode");
            digester.addRule(nodeRule, new SubProcessDefinitionPopRule());
        } else {
            digester.addSetNext(nodeRule, "addNode");
        }

        final String listenerRule = nodeRule + "/" + ELEMENT_LISTENER;
        digester.addObjectCreate(listenerRule, null, "class");
        digester.addSetNext(listenerRule, "addExecutionListener");
    }

    /**
     * For sequenceFlow rules
     */
    private void addSequenceFlowRule(Digester digester) {
        final String listenerRule = "*/" + ELEMENT_FLOW;
        digester.addRule(listenerRule, new ObjectCreateRule(SequenceFlow.class));
        digester.addSetProperties(listenerRule);
        digester.addSetNext(listenerRule, "addFLow");

        final String expressionRule = listenerRule + "/" + ELEMENT_CONDITION;
        digester.addRule(expressionRule, new ObjectCreateRule(JEXLConditionExpression.class));
        digester.addSetProperties(expressionRule);
        digester.addSetNext(expressionRule, "setExpression");
    }

    /**
     * The rule will push sub definition instance to stack when reach at beginning of "subProcess" node
     */
    static class SubProcessDefinitionCreateRule extends Rule {
        @Override
        public void begin(String namespace, String name, Attributes attributes) throws Exception {
            DefaultSubProcess subProcess = getDigester().peek();

            XMLDefinition parent = getDigester().peek(1);
            XMLDefinition subDefinition = new XMLDefinition(parent);
            subDefinition.setParentNode(subProcess);
            subProcess.setSubProcessDefinition(subDefinition);
            parent.addSubDefinition(subProcess.getId(), subDefinition);

            getDigester().push(subDefinition);
        }
    }

    /**
     * The rule will pop the sub definition instance from stack when reaching at the ends of "subProcess" node
     */
    static class SubProcessDefinitionPopRule extends Rule {
        @Override
        public void end(String namespace, String name) throws Exception {
            getDigester().pop();
        }
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
}
