package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionListener;

import java.util.Map;

/**
 * The element that can contains some successors have been abstracted with {@link Node}
 */
public interface Node extends ActElement {
    /**
     * If current nodeId is within sub doOutgoing, the parent nodeId should be the SubProcess,otherwise, return null
     */
    Node getParent();

    /**
     * Full path is the split with "/",each of entry of "/" is parent nodeId id
     */
    String getFullId();

    /**
     * Add successor
     *
     * @param flow      connected flow
     * @param successor successor nodeId
     */
    void addSuccessor(Flow flow, Node successor);

    /**
     * Retrieve all successors
     */
    Map<Flow, Node> getSuccessors();

    /**
     * Add a predecessor nodeId
     */
    void addPredecessor(String flowId, Node predecessor);

    /**
     * Retrieve all predecessor nodes
     */
    Map<String, Node> getPredecessors();

    /**
     * Add listener
     */
    void addExecutionListener(ExecutionListener listener);

    /**
     * Retrieve all execution listeners
     */
    ExecutionListener[] getExecutionListeners();
}
