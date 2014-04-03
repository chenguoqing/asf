package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionListener;

import java.util.Map;

/**
 * The base element interface which can be draw on the doOutgoing definition graph.All elements can be classified with:
 * <p>
 * <li>Flow: Connections between the <strong>ExecutionTask and Event</strong>
 * <li>Event: Start event or End event
 * <li>ExecutionTask: Execute the user logic
 * <li>Gateway: dispatch the flows by condition
 * <li>SubProcess: sub doOutgoing
 * </p>
 */
public interface Node {

    /**
     * Retrieve associated definition instance
     */
    ASFDefinition getDefinition();

    /**
     * Unique id
     */
    void setId(String id);

    /**
     * Unique id
     */
    String getId();

    /**
     * Element name
     */
    void setName(String name);

    /**
     * Retrieve element name
     */
    String getName();

    /**
     * Element description
     */
    void setDescription(String description);

    /**
     * Retrieve element description
     */
    String getDescription();

    /**
     * Element type
     */
    ActType getType();

    /**
     * Set parent
     */
    void setParent(Node parent);

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
    void addPredecessor(Flow flow, Node predecessor);

    /**
     * Retrieve all predecessor nodes
     */
    Map<Flow, Node> getPredecessors();

    /**
     * Add listener
     */
    void addExecutionListener(ExecutionListener listener);

    /**
     * Retrieve all execution listeners
     */
    ExecutionListener[] getExecutionListeners();
}
