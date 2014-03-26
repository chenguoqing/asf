package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

/**
 * When flows to asf node, the associated {@code ExecutionProcessor} will be executed;{@link com.baidu.asf.engine
 * .processor.ExecutionProcessor}  only be visible within process engine; it should NOT be accessed by external api.
 */
public interface ExecutionProcessor {
    /**
     * The method will be invoked when flows incoming to current nodeId
     *
     * @param context processor context
     * @param node    current node
     * @param from    from node
     * @param flow    sequence flow
     */
    void doIncoming(ProcessorContext context, Node node, Node from, Flow flow);

    /**
     * The method will be invoked when leaving current nodeId
     *
     * @param context processor context
     * @param node    current node
     */
    void doOutgoing(ProcessorContext context, Node node);
}
