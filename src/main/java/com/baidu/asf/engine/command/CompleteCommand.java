package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.engine.processor.ExecutionProcessor;
import com.baidu.asf.engine.processor.ExecutionProcessorRegister;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.Node;

/**
 * {@link CompleteCommand} will dispatch the complete task to special processor
 */
public class CompleteCommand implements Command<Void> {

    private final Node node;

    public CompleteCommand(Node userTask) {
        this.node = userTask;
    }

    @Override
    public Void execute(ProcessorContext context) {
        ExecutionProcessor processor = ExecutionProcessorRegister.getProcessor(ActType.UserTask);
        processor.doOutgoing(context, node);
        return null;
    }
}
