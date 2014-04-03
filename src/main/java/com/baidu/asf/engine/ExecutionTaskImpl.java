package com.baidu.asf.engine;

import com.baidu.asf.model.UserTask;

import java.util.Map;

/**
 * Default implementation of ExecutionTask
 */
public class ExecutionTaskImpl implements ExecutionTask {
    /**
     * ID
     */
    private final long executionId;
    /**
     * Associated node
     */
    private final UserTask userTask;

    /**
     * associated instance
     */
    private final ASFInstance instance;

    public ExecutionTaskImpl(long executionId, UserTask userTask, ASFInstance instance) {
        this.executionId = executionId;
        this.userTask = userTask;
        this.instance = instance;
    }

    @Override
    public long getExecutionId() {
        return 0;
    }

    @Override
    public UserTask getUserTask() {
        return userTask;
    }

    @Override
    public ASFInstance getInstance() {
        return instance;
    }

    @Override
    public void complete() {
        this.instance.complete(executionId);
    }

    @Override
    public void complete(Map<String, Object> variables) {
        this.instance.complete(executionId, variables);
    }
}
