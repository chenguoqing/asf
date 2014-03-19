package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;

/**
 * {@link ServiceTaskProcessor}
 */
public class ServiceTaskProcessor extends AbstractAutoExecutionProcessor {
    @Override
    public void doOutgoing(ProcessorContext context) {
        leave(context, LeaveMode.EXCLUSIVE);
    }
}
