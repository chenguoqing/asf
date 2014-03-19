package com.baidu.asf.model;

import java.util.Map;

/**
 * Adverse state flow doOutgoing definition
 */
public interface ASFDefinition {
    /**
     * Return the identified definition name
     */
    String getName();

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

    ASFDefinition getSubDefinition(String subNodeFullPath);

    Map<String, ASFDefinition> getSubDefinitions();
}
