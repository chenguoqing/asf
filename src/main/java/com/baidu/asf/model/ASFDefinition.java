package com.baidu.asf.model;

import java.util.Map;

/**
 * {@link ASFDefinition} represents a process definition interface,the interface is readonly for users
 */
public interface ASFDefinition {
    /**
     * Return the unique id
     */
    String getId();

    /**
     * Return the process description
     */
    String getDescription();

    /**
     * Retrieve the definition modified version
     */
    int getVersion();

    /**
     * Retrieve the {@link StartEvent}, doOutgoing must start with StartEvent,end with EndEvent
     */
    StartEvent getStartEvent();

    /**
     * Retrieve the {@link EndEvent}, doOutgoing must start with StartEvent,end with EndEvent
     */
    EndEvent getEndEvent();

    /**
     * Retrieve the element by id,it only search the namespace of current doOutgoing(not including sub doOutgoing)
     */
    <T extends Node> T getNode(String id);

    /**
     * Find the element by id path(path is split by "a/b/c",)
     */
    <T extends Node> T findNode(String path);

    /**
     * Return parent definition
     */
    ASFDefinition getParent();

    /**
     * Retrieve sub definition by sub definition id
     */
    ASFDefinition getSubDefinition(String subDefinitionId);

    /**
     * Retrieve all sub definitions
     */
    Map<String, ASFDefinition> getSubDefinitions();
}
