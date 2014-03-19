package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;

/**
 * {@link StartEventProcessor} will start the process
 */
public class StartEventProcessor extends AbstractAutoExecutionProcessor {
    @Override
    public void doOutgoing(ProcessorContext context) {
        leave(context, LeaveMode.EXCLUSIVE);
    }
}
