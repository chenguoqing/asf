package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

import java.util.Iterator;
import java.util.Map;

/**
 * When flows to parallel gateway, it should automatically dispatch all paths
 */
public class ParallelGatewayProcessor extends AbstractExecutionProcessor {

    private static final String VARIABLE_JOIN_NODE_COUNT = "_joinCount_parallel";

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
        Map<Flow, Node> successors = node.getSuccessors();

        Flow defaultFlow = successors.keySet().iterator().next();
        Node defaultTarget = successors.get(defaultFlow);

        if (successors.size() == 1) {
            doLeaving(context, node, defaultFlow, defaultTarget);
            return;
        }

        for (Iterator<Flow> it = successors.keySet().iterator(); it.hasNext(); ) {
            Flow flow = it.next();
            doLeaving(context, node, flow, successors.get(flow));
        }
    }
}
