package com.baidu.asf.model;

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
public interface ActElement {
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
}

