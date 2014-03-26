package com.baidu.asf.engine.processor;

import com.baidu.asf.ASFException;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.engine.ProcessorContextImpl;
import com.baidu.asf.model.*;

/**
 * {@link DefaultSubProcessProcessor} will start a new sub process automatically
 */
public class DefaultSubProcessProcessor extends AbstractExecutionProcessor implements SubProcessProcessor {

    @Override
    public void doIncoming(ProcessorContext context, Node node, Node from, Flow flow) {
        super.doIncoming(context, node, from, flow);

        SubProcess subProcess = (SubProcess) node;
        ASFDefinition subProcessDefinition = subProcess.getSubProcessDefinition();
        StartEvent startEvent = subProcessDefinition.getStartEvent();

        ProcessorContextImpl subContext = new ProcessorContextImpl();
        subContext.setDefinition(subProcessDefinition);
        subContext.addParams(context.getParams());
        subContext.setInstance(context.getInstance());

        // start sub process
        startSubProcess(subProcess, subContext);
    }

    /**
     * Start the sub process, add a virtual flow from SubProcess to StartEvent(sub process)
     */
    @Override
    public void doOutgoing(ProcessorContext context, Node node) {
        leave(context, node, LeaveMode.EXCLUSIVE);
    }

    @Override
    public void startSubProcess(SubProcess subProcess, ProcessorContext subContext) {
        ExecutionProcessor startEventProcessor = ExecutionProcessorRegister.getProcessor(ActType.StartEvent);
        StartEvent startEvent = subProcess.getSubProcessDefinition().getStartEvent();
        Flow flow = new SequenceFlow(subProcess.getFullId(), startEvent.getFullId());
        startEventProcessor.doIncoming(subContext, startEvent, subProcess, flow);
    }

    @Override
    public void endSubProcess(ProcessorContext subContext) {
        StartEvent startEvent = subContext.getDefinition().getStartEvent();

        SubProcess subProcess = (SubProcess) startEvent.getParent();

        if (subProcess == null) {
            throw new ASFException("Invalidate nodeId path:" + startEvent.getFullId());
        }

        // adds a virtual flow from EndEvent of sub process to SubProcess for execution tracing,
        // but don't fire the listeners
        ProcessorContextImpl parentContext = new ProcessorContextImpl();
        parentContext.setDefinition(subContext.getDefinition().getParent());
        parentContext.setInstance(subContext.getInstance());
        parentContext.setEntityManager(subContext.getEntityManager());

        EndEvent endEvent = subContext.getDefinition().getEndEvent();
        Flow flow = new SequenceFlow(endEvent.getFullId(), subProcess.getFullId());

        // tracing execution history
        traceExecution(parentContext, endEvent, subContext.getDefinition().getEndEvent(), flow);

        // switch the process context to parent and continue
        doOutgoing(parentContext, endEvent);
    }
}
