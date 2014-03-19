package com.baidu.asf.engine;

import com.baidu.asf.model.ASFDefinition;

import java.util.List;
import java.util.Map;

/**
 * Process instance interface
 */
public interface ASFInstance extends VariableContext, SystemVariableContext {
    /**
     * ASF doOutgoing status
     */
    enum ASFStatus {
        ACTIVE(0),
        SUSPEND(1),
        ENDED(2);

        public final int value;

        ASFStatus(int value) {
            this.value = value;
        }

        public static ASFStatus get(int value) {
            if (value < 0 || value >= values().length) {
                throw new IllegalArgumentException("value is invalidate.");
            }
            return values()[value];
        }
    }

    /**
     * Retrieve associated doOutgoing definition
     */
    ASFDefinition getDefinition();

    /**
     * Instance id
     */
    long getId();

    /**
     * Retrieve current status
     */
    ASFStatus getStatus();

    /**
     * Retrieve all UserTask associated current doOutgoing
     */
    List<ExecutionTask> getTasks();

    /**
     * Retrieve the execution path of current doOutgoing,it will back the execution path until to StartEvent
     */
    ExecutionTracingNode getExecutionPath();

    /**
     * Transit the flow to nodeId nodeId,all outgoing flows will be evaluated, one of true will be selected.
     */
    void complete(long executionTaskId);

    /**
     * Transit the flow to nodeId nodeId,all outgoing flows will be evaluated, one of true will be selected.
     *
     * @param variables initial variables
     */
    void complete(long executionTaskId, Map<String, Object> variables);

    /**
     * Pause the doOutgoing instance, it will throw {@link com.baidu.asf.engine.ASFStatusException} if the instance has
     * ended
     */
    void pause();

    /**
     * Resume the instance execution, it will throw {@link com.baidu.asf.engine.ASFStatusException} of the instance
     * is not on pause status
     */
    void resume();
}
