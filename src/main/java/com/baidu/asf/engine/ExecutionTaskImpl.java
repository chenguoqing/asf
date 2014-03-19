package com.baidu.asf.engine;

import com.baidu.asf.model.ActType;

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
     * Act id
     */
    private final String actId;
    /**
     * Task name
     */
    private final String name;
    /**
     * Act type
     */
    private final ActType actType;
    /**
     * Task description
     */
    private String description;

    /**
     * associated instance
     */
    private final ASFInstance instance;

    public ExecutionTaskImpl(long executionId, String actId, String name, ActType actType, ASFInstance instance) {
        this.executionId = executionId;
        this.name = name;
        this.actId = actId;
        this.actType = actType;
        this.instance = instance;
    }

    @Override
    public long getExecutionId() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getActId() {
        return actId;
    }

    @Override
    public ActType getActType() {
        return actType;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
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
