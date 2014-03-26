package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

/**
 * For some auto tasks(ServiceTask,ParallelGateway,ExclusiveGateway,InclusiveGateway,
 * SubProcess) should driven the flows to nodeId nodeId automatically
 */
public abstract class AbstractAutoExecutionProcessor extends AbstractExecutionProcessor {
    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);
        doOutgoing(context, node);
    }
}
