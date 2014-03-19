package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

/**
 * When flows to asf nodeId, the associated {@code ExecutionProcessor} will be executed;{@link com.baidu.asf.engine
 * .processor.ExecutionProcessor}  only be visible within process engine; it should NOT be accessed by external api.
 */
public interface ExecutionProcessor {
    /**
     * The method will be invoked when flows incoming to current nodeId
     */
    void doIncoming(ProcessorContext context, Node from, Flow flow);

    /**
     * The method will be invoked when leaving current nodeId
     */
    void doOutgoing(ProcessorContext context);
}
