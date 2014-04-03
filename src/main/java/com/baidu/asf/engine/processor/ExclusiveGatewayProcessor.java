package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

import java.util.Iterator;
import java.util.Map;

/**
 * {@link com.baidu.asf.model.ExclusiveGateway} semantic
 */
public class ExclusiveGatewayProcessor extends AbstractAutoExecutionProcessor {
    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        doExclusiveLeave(context, node);
    }
}
