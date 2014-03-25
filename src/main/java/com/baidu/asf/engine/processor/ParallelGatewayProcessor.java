package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

import java.util.Map;

/**
 * When flows to parallel gateway, it should automatically dispatch all paths
 */
public class ParallelGatewayProcessor extends AbstractExecutionProcessor {

    private static final String VARIABLE_JOIN_NODE_COUNT = "_joinCount_parallel";

    @Override
    public void doIncoming(ProcessorContext context, Node from, Flow flow) {
        super.doIncoming(context, from, flow);
        Map<Flow, Node> predecessors = context.getNode().getPredecessors();

        // join
        if (predecessors.size() == 1) {
            doOutgoing(context);
        } else {
            String variableName = context.getNode().getFullId() + VARIABLE_JOIN_NODE_COUNT;

            Integer count = context.getInstance().incrementAndGet(variableName);

            // all predecessors has arrived
            if (count == context.getNode().getPredecessors().size()) {
                doOutgoing(context);
            }
        }
    }

    @Override
    public void doOutgoing(ProcessorContext context) {
        leave(context, LeaveMode.PARALLEL);
    }
}
