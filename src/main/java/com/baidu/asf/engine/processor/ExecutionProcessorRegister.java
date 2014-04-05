package com.baidu.asf.engine.processor;

import com.baidu.asf.model.NodeType;

import java.util.HashMap;
import java.util.Map;

/**
 * Process register
 */
public class ExecutionProcessorRegister {
    private static final Map<NodeType, ExecutionProcessor> processors = new HashMap<NodeType, ExecutionProcessor>();

    // register processors for all act sourceRef
    static {
        processors.put(NodeType.StartEvent, new StartEventProcessor());
        processors.put(NodeType.UserTask, new UserTaskProcessor());
        processors.put(NodeType.ServiceTask, new ServiceTaskProcessor());
        processors.put(NodeType.ParallelGateway, new ParallelGatewayProcessor());
        processors.put(NodeType.ExclusiveGateway, new ExclusiveGatewayProcessor());
        processors.put(NodeType.InclusiveGateway, new InclusiveGatewayProcessor());
        processors.put(NodeType.SubProcess, new DefaultSubProcessProcessor());
        processors.put(NodeType.EndEvent, new EndEventProcessor());
    }

    public static ExecutionProcessor getProcessor(NodeType actType) {
        return processors.get(actType);
    }
}
