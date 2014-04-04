package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ASFInstance;
import com.baidu.asf.engine.ASFInstanceImpl;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

/**
 * {@link EndEventProcessor}
 */
public class EndEventProcessor extends AbstractExecutionProcessor {
    private static final String VARIABLE_END_COUNT = "_endCount";

    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);

        // If current process is top process
        if (node.getParent() == null) {
            // end the process
            if (node.getPredecessors().size() > 1) {
                String variableName = node.getFullId() + VARIABLE_END_COUNT;
                int count = context.getInstance().incrementAndGet(variableName);
                if (count < node.getPredecessors().size()) {
                    return;
                }
            }
            // mark process instance end
            ((ASFInstanceImpl) context.getInstance()).setStatus(ASFInstance.ASFStatus.ENDED);

            // clear all user and system variables
            context.getInstance().clearVariables();
            context.getInstance().clearSystemVariables();
        } else {
            doOutgoing(context, node);
        }
    }

    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        // only the sub process sourceRef can be processed
        if (node.getParent() != null) {
            SubProcessProcessor processor = (SubProcessProcessor) ExecutionProcessorRegister.getProcessor(ActType.SubProcess);
            processor.endSubProcess(context);
        }
    }
}
