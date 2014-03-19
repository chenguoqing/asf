package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ExecutionTracingNode;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.ActType;
import com.baidu.asf.persistence.enitity.TransitionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find the execution path for current instance
 */
public class GetExecutionPathCommand implements Command<ExecutionTracingNode> {
    @Override
    public ExecutionTracingNode execute(ProcessorContext context) {
        List<TransitionEntity> transitionEntities = context.getEntityManager().findTransitions(context.getInstance()
                .getId());

        if (transitionEntities == null) {
            return null;
        }
        String startEventId = transitionEntities.get(0).getFromActFullPath();
        Map<String, ExecutionTracingNode> executionNodeMap = new HashMap<String, ExecutionTracingNode>();

        for (TransitionEntity entity : transitionEntities) {
            ExecutionTracingNode from = getAndCreate(entity.getFromActFullPath(), executionNodeMap);
            ExecutionTracingNode to = getAndCreate(entity.getToActFullPath(), executionNodeMap);

            // remove the virtual connection(from EndEvent of sub process to SubProcess)
            if (entity.getFromActType() != ActType.EndEvent || !entity.isVirtualFlow()) {
                from.addSuccessor(entity.getFlowId(), to);
            }
        }

        return executionNodeMap.get(startEventId);
    }

    private ExecutionTracingNode getAndCreate(String actFullId, Map<String, ExecutionTracingNode> executionNodeMap) {
        ExecutionTracingNode node = executionNodeMap.get(actFullId);

        if (node == null) {
            node = new ExecutionTracingNode(actFullId);
            executionNodeMap.put(actFullId, node);
        }
        return node;
    }

}
