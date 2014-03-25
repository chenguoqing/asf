package com.baidu.asf.engine.processor;

import com.baidu.asf.ASFException;
import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.model.*;

/**
 * {@link DefaultSubProcessProcessor} will start a new sub process automatically
 */
public class DefaultSubProcessProcessor extends AbstractExecutionProcessor implements SubProcessProcessor {

    private static final String START_SUBPROCESS_FLOW_ID = "__startSubprocess__";
    private static final String END_SUBPROCESS_FLOW_ID = "__endSubprocess__";

    @Override
    public void doIncoming(ProcessorContext context, Node from, Flow flow) {
        super.doIncoming(context, from, flow);

        SubProcess subProcess = context.getNode();
        ASFDefinition subProcessDefinition = subProcess.getSubProcessDefinition();
        StartEvent startEvent = subProcessDefinition.getStartEvent();

        ProcessorContext subContext = context.newContext(subProcessDefinition, startEvent);

        // start sub process
        startSubProcess(subProcess, subContext);
    }

    /**
     * Start the sub process, add a virtual flow from SubProcess to StartEvent(sub process)
     */
    @Override
    public void doOutgoing(ProcessorContext context) {
        leave(context, LeaveMode.EXCLUSIVE);
    }

    @Override
    public void startSubProcess(SubProcess subProcess, ProcessorContext subContext) {
        ExecutionProcessor startEventProcessor = ExecutionProcessorRegister.getProcessor(ActType.StartEvent);
        startEventProcessor.doIncoming(subContext, subProcess, new SequenceFlow(subProcess.getFullId(),
                subProcess.getSubProcessDefinition().getStartEvent().getFullId(), true,
                null));
    }

    @Override
    public void endSubProcess(ProcessorContext subContext) {
        String fullPath = subContext.getDefinition().getStartEvent().getFullId();
        int index = fullPath.lastIndexOf("/");
        String subProcessFullPath = fullPath.substring(0, index);
        Node node = subContext.getInstance().getDefinition().findNode(subProcessFullPath);

        if (node == null) {
            throw new ASFException("Invalidate nodeId path:" + fullPath);
        }

        if (!(node instanceof SubProcess)) {
            throw new ASFException("Expect SubProcess nodeId type for path:" + subProcessFullPath);
        }

        SubProcess subProcess = (SubProcess) node;

        // adds a virtual flow from EndEvent of sub process to SubProcess for execution tracing,
        // but don't fire the listeners
        ProcessorContext parentContext = subContext.newContext(subContext.getDefinition().getParent(), subProcess);
        traceExecution(parentContext, subContext.getDefinition().getEndEvent(),
                new SequenceFlow(subContext.getDefinition().getEndEvent().getFullId(), node.getFullId(), true, null));

        // switch the process context to parent and continue
        doOutgoing(parentContext);
    }
}
