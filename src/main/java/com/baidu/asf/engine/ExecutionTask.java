package com.baidu.asf.engine;

import com.baidu.asf.model.ActType;

import java.util.Map;

/**
 * {@link ExecutionTask} is a execution unit of {@link com.baidu.asf.model.UserTask},
 * it wrappers runtime information like the {@link ExecutionTask}.
 */
public interface ExecutionTask {

    /**
     * The execution id
     */
    long getExecutionId();

    /**
     * Act id
     */
    String getActId();

    /**
     * ExecutionTask name(act id)
     */
    String getName();

    /**
     * Act type
     */
    ActType getActType();

    /**
     * ExecutionTask description
     */
    String getDescription();

    /**
     * Associated doOutgoing instance
     */
    ASFInstance getInstance();

    /**
     * Continues to nodeId nodeId
     */
    void complete();

    /**
     * Continues to nodeId nodeId with variables
     */
    void complete(Map<String, Object> variables);
}
