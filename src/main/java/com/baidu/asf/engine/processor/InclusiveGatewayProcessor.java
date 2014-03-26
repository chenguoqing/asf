package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

import java.util.Map;

/**
 * Created by chenguoqing01 on 14-3-6.
 */
public class InclusiveGatewayProcessor extends AbstractAutoExecutionProcessor {
    private static final String VARIABLE_JOIN_NODE_COUNT = "_joinCount_inclusive";

    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);
        Map<Flow, Node> predecessors = node.getPredecessors();

        // join
        if (predecessors.size() == 1) {
            doOutgoing(context, node);
        } else {
            String variableName = node.getFullId() + VARIABLE_JOIN_NODE_COUNT;

            Integer count = context.getInstance().incrementAndGet(variableName);

            // all predecessors has arrived
            if (count == node.getPredecessors().size()) {
                doOutgoing(context, node);
            }
        }
    }

    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        leave(context, node, LeaveMode.INCLUSIVE);
    }
}
