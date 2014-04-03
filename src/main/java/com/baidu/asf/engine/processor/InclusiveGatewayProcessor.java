package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;
import com.baidu.asf.util.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The semantic implementation of inclusive gateway,because of the complication of <b>join</b> behaviour
 * ({@link com.baidu.asf.model.InclusiveGateway}),only one level back path is supported:
 * <pre>
 *     When forks some outgoing sequence flows from inclusive gateway(for node), if some of flows are not be chosen,
 *     the respective successors can be marked as "no-execution" nodes; when join behaviour occurs on another
 *     inclusive gateway(join node), it only simply ignore some incoming flows by "no-execution" marks.
 * </pre>
 */
public class InclusiveGatewayProcessor extends AbstractExecutionProcessor {

    static enum JoinType {
        NO_EXECUTION(0),
        ARRIVE(1),
        NO_ARRIVE(2);
        public final int type;

        JoinType(int type) {
            this.type = type;
        }
    }

    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);
        Map<Flow, Node> predecessors = node.getPredecessors();

        // join
        if (predecessors.size() == 1) {
            doOutgoing(context, node);
            return;
        }

        final String joinMapVariableName = node.getFullId() + Constants.VARIABLE_JOIN_NODE_MAP;
        Map<String, JoinType> joinMap = (Map<String, JoinType>) context.getInstance().getSystemVariable
                (joinMapVariableName);

        if (joinMap == null) {
            joinMap = new HashMap<String, JoinType>();
            for (Flow f : predecessors.keySet()) {
                Node predecessor = predecessors.get(f);
                final String noExecutionVariableName = predecessor.getFullId() + Constants
                        .VARIABLE_NO_EXECUTION_NODE;

                Object v = context.getInstance().getSystemVariable(noExecutionVariableName);

                // if current predecessor is no-execution node
                if (v != null && Boolean.valueOf(v.toString())) {
                    joinMap.put(predecessor.getFullId(), JoinType.NO_EXECUTION);
                } else {
                    joinMap.put(predecessor.getFullId(), JoinType.NO_ARRIVE);
                }
            }
        }

        joinMap.put(from.getFullId(), JoinType.ARRIVE);

        context.getInstance().setSystemVariable(joinMapVariableName, joinMap);

        // having some flows is not arrived yet.
        for (JoinType type : joinMap.values()) {
            if (type == JoinType.NO_ARRIVE) {
                return;
            }
        }

        // delete "no-execution" nodes mark
        for (String nodeId : joinMap.keySet()) {
            JoinType type = joinMap.get(nodeId);
            if (type == JoinType.NO_EXECUTION) {
                context.getInstance().removeSystemVariable(nodeId + Constants.VARIABLE_NO_EXECUTION_NODE);
            }
        }

        // remove join map variable
        context.getInstance().removeSystemVariable(joinMapVariableName);

        // all predecessors has arrived
        doOutgoing(context, node);
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

            // for inclusive, if the flow is evaluated to true,the flow should be executed
            if (flow.evaluate(context.getInstance())) {
                doLeaving(context, node, flow, successors.get(flow));
                // if the flow is evaluated to false,it should mark the target node as no-executions node
            } else {
                Node successor = successors.get(flow);
                final String variableName = successor.getFullId() + Constants.VARIABLE_NO_EXECUTION_NODE;
                context.getInstance().setSystemVariable(variableName, true);
            }
        }
    }
}
