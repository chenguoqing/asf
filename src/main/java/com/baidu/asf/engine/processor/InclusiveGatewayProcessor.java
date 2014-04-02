package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;
import com.baidu.asf.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * The semantic implementation of inclusive gateway
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

        // all predecessors has arrived
        doOutgoing(context, node);
    }

    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        leave(context, node, LeaveMode.INCLUSIVE);
    }
}
