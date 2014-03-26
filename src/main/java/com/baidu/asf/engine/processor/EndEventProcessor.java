package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

/**
 * {@link EndEventProcessor}
 */
public class EndEventProcessor extends AbstractExecutionProcessor {
    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);

        // If the EndEvent belongs to top process, it should remove all execution associated current instance
        if (node.getParent() == null) {
            context.getEntityManager().removeExecutions(context.getInstance().getId());
        }

        doOutgoing(context, node);
    }

    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        // only the sub process nodeId can be processed
        if (node.getParent() != null) {
            SubProcessProcessor processor = (SubProcessProcessor) ExecutionProcessorRegister.getProcessor(ActType.SubProcess);
            processor.endSubProcess(context);
        }
    }
}
