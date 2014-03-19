package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.engine.processor.*;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link CompleteCommand} will dispatch the complete task to special processor
 */
public class CompleteCommand implements Command<Void> {

    private static final Map<ActType, ExecutionProcessor> processors = new HashMap<ActType, ExecutionProcessor>();

    // register processors for all act nodeId
    static {
        processors.put(ActType.UserTask, new UserTaskProcessor());
        processors.put(ActType.ServiceTask, new ServiceTaskProcessor());
        processors.put(ActType.ParallelGateway, new ParallelGatewayProcessor());
        processors.put(ActType.ExclusiveGateway, new ExclusiveGatewayProcessor());
        processors.put(ActType.InclusiveGateway, new InclusiveGatewayProcessor());
        processors.put(ActType.SubProcess, new DefaultSubProcessProcessor());
        processors.put(ActType.EndEvent, new EndEventProcessor());
    }

    private final Node node;

    public CompleteCommand(Node userTask) {
        this.node = userTask;
    }

    @Override
    public Void execute(ProcessorContext context) {
        ExecutionProcessor processor = processors.get(node.getType());
        processor.doOutgoing(context);
        return null;
    }
}
