package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ASFConcurrentModificationException;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;
import com.baidu.asf.persistence.MVCCException;
import com.baidu.asf.persistence.enitity.ExecutionEntity;

/**
 * {@link UserTaskProcessor} will save current execution when incoming, and remove it when leaving
 */
public class UserTaskProcessor extends AbstractExecutionProcessor {
    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);

        // Create user task execution
        ExecutionEntity entity = new ExecutionEntity();
        entity.setNodeFullId(node.getFullId());
        entity.setActType(ActType.UserTask);
        entity.setInstanceId(context.getInstance().getId());

        context.getEntityManager().createExecution(entity);
    }

    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        // remove user task
        try {
            context.getEntityManager().removeExecution(context.getExecutionTaskId());
        } catch (MVCCException e) {
            throw new ASFConcurrentModificationException(context.getInstance());
        }

        // leave UserTask
        leave(context, node, LeaveMode.EXCLUSIVE);
    }
}
