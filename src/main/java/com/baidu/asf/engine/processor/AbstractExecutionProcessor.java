package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ExecutionEvent;
import com.baidu.asf.engine.ExecutionListener;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;
import com.baidu.asf.persistence.enitity.TransitionEntity;

import java.util.Iterator;
import java.util.Map;

/**
 * The abstract implementation will notice all {@link com.baidu.asf.engine.ExecutionListener}
 */
public abstract class AbstractExecutionProcessor implements ExecutionProcessor {

    static enum LeaveMode {
        EXCLUSIVE,
        PARALLEL,
        INCLUSIVE
    }

    /**
     * Do some common processing for all processors
     */
    @Override
    public void doIncoming(ProcessorContext context, Node from, Flow flow) {
        // save transition history
        traceExecution(context, from, flow);

        // notify all listeners
        final ExecutionEvent event = new ExecutionEvent(context.getInstance(), from, flow, context.getNode());
        ExecutionListener[] listeners = context.getNode().getExecutionListeners();

        if (listeners != null) {
            for (ExecutionListener listener : listeners) {
                listener.onNode(event);
            }
        }
    }

    protected void traceExecution(ProcessorContext context, Node from, Flow flow) {
        // save transition history
        TransitionEntity transitionEntity = new TransitionEntity();
        transitionEntity.setInstanceId(context.getInstance().getId());
        transitionEntity.setFromActFullId(from.getFullPath());
        transitionEntity.setFromActType(from.getType());
        transitionEntity.setToActFullId(context.getNode().getFullPath());
        transitionEntity.setToActType(context.getNode().getType());
        transitionEntity.setFlowId(flow.getId());
        transitionEntity.setVirtualFlow(flow.isVirtual());

        context.getEntityManager().createTransition(transitionEntity);
    }

    /**
     * Leaves current nodeId with mode
     */
    protected void leave(ProcessorContext context, LeaveMode mode) {
        Map<Flow, Node> successors = context.getNode().getSuccessors();

        Flow defaultFlow = successors.keySet().iterator().next();
        Node defaultTarget = successors.get(defaultFlow);

        if (successors.size() == 1) {
            doLeaving(context, defaultFlow, defaultTarget);
            return;
        }

        for (Iterator<Flow> it = successors.keySet().iterator(); it.hasNext(); ) {
            Flow flow = it.next();
            if (mode == LeaveMode.PARALLEL || flow.evaluate(context.getInstance())) {
                doLeaving(context, flow, successors.get(flow));
                if (mode == LeaveMode.EXCLUSIVE) {
                    break;
                }
            }
        }
    }

    protected void doLeaving(ProcessorContext context, Flow flow, Node target) {
        ExecutionProcessor targetProcessor = ExecutionProcessorRegister.getProcessor(target.getType());
        targetProcessor.doIncoming(context, context.getNode(), flow);
    }
}
