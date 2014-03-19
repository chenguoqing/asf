package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;

/**
 * {@link com.baidu.asf.model.ExclusiveGateway} semantic
 */
public class ExclusiveGatewayProcessor extends AbstractAutoExecutionProcessor {
    @Override
    public void doOutgoing(ProcessorContext context) {
        leave(context, LeaveMode.EXCLUSIVE);
    }
}
