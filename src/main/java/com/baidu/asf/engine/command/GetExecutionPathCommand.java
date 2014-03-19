package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ExecutionPathNode;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.persistence.enitity.TransitionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find the execution path for current instance
 */
public class GetExecutionPathCommand implements Command<ExecutionPathNode> {
    @Override
    public ExecutionPathNode execute(ProcessorContext context) {
        List<TransitionEntity> transitionEntities = context.getEntityManager().findTransitions(context.getInstance()
                .getId());

        if (transitionEntities == null) {
            return null;
        }
        String startEventId = transitionEntities.get(0).getFromActFullId();
        Map<String, ExecutionPathNode> executionNodeMap = new HashMap<String, ExecutionPathNode>();

        for (TransitionEntity entity : transitionEntities) {
            ExecutionPathNode from = getAndCreate(entity.getFromActFullId(), executionNodeMap);
            ExecutionPathNode to = getAndCreate(entity.getToActFullId(), executionNodeMap);
            from.addSuccessor(entity.getFlowId(), to);
        }

        return executionNodeMap.get(startEventId);
    }

    private ExecutionPathNode getAndCreate(String actFullId, Map<String, ExecutionPathNode> executionNodeMap) {
        ExecutionPathNode node = executionNodeMap.get(actFullId);

        if (node == null) {
            node = new ExecutionPathNode(actFullId);
            executionNodeMap.put(actFullId, node);
        }
        return node;
    }

}
