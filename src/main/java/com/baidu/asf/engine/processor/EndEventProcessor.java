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
    public void doIncoming(ProcessorContext context, Node from, Flow flow) {
        super.doIncoming(context, from, flow);

        // If the EndEvent belongs to top process, it should remove all execution associated current instance
        if (context.getNode().getParent() == null) {
            context.getEntityManager().removeExecutions(context.getInstance().getId());
        }

        doOutgoing(context);
    }

    @Override
    public void doOutgoing(ProcessorContext context) {
        // only the sub process nodeId can be processed
        if (context.getNode().getParent() != null) {
            SubProcessProcessor processor = (SubProcessProcessor) ExecutionProcessorRegister.getProcessor(ActType.SubProcess);
            processor.endSubProcess(context);
        }
    }
}
