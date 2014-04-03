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

    /**
     * Do some common processing for all processors
     */
    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        // save transition history
        traceTransition(context, node, from, flow);

        // notify all listeners
        final ExecutionEvent event = new ExecutionEvent(context.getInstance(), from, flow, node);
        ExecutionListener[] listeners = node.getExecutionListeners();

        if (listeners != null) {
            for (ExecutionListener listener : listeners) {
                listener.onNode(event);
            }
        }
    }

    protected void traceTransition(ProcessorContext context, Node node, Node from, Flow flow) {
        // save transition history
        TransitionEntity transitionEntity = new TransitionEntity();
        transitionEntity.setInstanceId(context.getInstance().getId());
        transitionEntity.setFromActFullId(from.getFullId());
        transitionEntity.setFromActType(from.getType());
        transitionEntity.setToActFullId(node.getFullId());
        transitionEntity.setToActType(node.getType());
        transitionEntity.setVirtualFlow(flow.isVirtual());

        context.getEntityManager().createTransition(transitionEntity);
    }

    /**
     * Leaves current nodeId with mode
     */
    protected void doExclusiveLeave(ProcessorContext context, Node node) {
        Map<Flow, Node> successors = node.getSuccessors();

        Flow defaultFlow = successors.keySet().iterator().next();
        Node defaultTarget = successors.get(defaultFlow);

        if (successors.size() == 1) {
            doLeaving(context, node, defaultFlow, defaultTarget);
            return;
        }

        defaultFlow = null;
        defaultTarget = null;

        for (Iterator<Flow> it = successors.keySet().iterator(); it.hasNext(); ) {
            Flow flow = it.next();

            // default flow condition evaluation will be ignored. If multiple default flow are exists,
            // the first one will be take
            if (flow.isDefault() && defaultFlow == null) {
                defaultFlow = flow;
                defaultTarget = successors.get(flow);
                continue;
            }

            if (flow.evaluate(context.getInstance())) {
                doLeaving(context, node, flow, successors.get(flow));
                return;
            }
        }

        // if the default flow exists
        if (defaultFlow != null) {
            doLeaving(context, node, defaultFlow, defaultTarget);
        }
    }

    protected void doLeaving(ProcessorContext context, Node node, Flow flow, Node target) {
        ExecutionProcessor targetProcessor = ExecutionProcessorRegister.getProcessor(target.getType());
        targetProcessor.doIncoming(context, target, node, flow);
    }
}
