package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Node;

/**
 * {@link com.baidu.asf.model.ExclusiveGateway} semantic
 */
public class ExclusiveGatewayProcessor extends AbstractAutoExecutionProcessor {
    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        leave(context, node, LeaveMode.EXCLUSIVE);
    }
}
