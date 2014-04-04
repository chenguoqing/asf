package com.baidu.asf.engine;

import com.baidu.asf.model.UserTask;

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
     * Retrieve associated user task node
     */
    UserTask getUserTask();

    /**
     * Associated doOutgoing instance
     */
    ASFInstance getInstance();

    /**
     * Continues to fromNodeId fromNodeId
     */
    void complete();

    /**
     * Continues to fromNodeId fromNodeId with variables
     */
    void complete(Map<String, Object> variables);
}
