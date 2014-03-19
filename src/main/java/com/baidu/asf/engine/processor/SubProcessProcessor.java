package com.baidu.asf.engine.processor;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.SubProcess;

/**
 * Sub process processor
 */
public interface SubProcessProcessor extends ExecutionProcessor {
    /**
     * Start sub process,the execution will downgrade to sub process
     *
     * @param subProcess current sub process nodeId
     * @param subContext the {@link com.baidu.asf.engine.ProcessorContext} for sub process
     */
    void startSubProcess(SubProcess subProcess, ProcessorContext subContext);

    /**
     * End sub process,the execution control will return to parent process
     *
     * @param subContext the {@link com.baidu.asf.engine.ProcessorContext} for sub process
     */
    void endSubProcess(ProcessorContext subContext);
}
