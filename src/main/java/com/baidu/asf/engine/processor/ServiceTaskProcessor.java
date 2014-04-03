package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Node;

/**
 * {@link ServiceTaskProcessor}
 */
public class ServiceTaskProcessor extends AbstractAutoExecutionProcessor {
    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        doExclusiveLeave(context, node);
    }
}
