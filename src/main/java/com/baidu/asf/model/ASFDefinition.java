package com.baidu.asf.model;

import java.io.IOException;
import java.util.Map;

/**
 * Adverse state flow doOutgoing definition
 */
public interface ASFDefinition extends ActElement {
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
     * Retrieve sub definition
     */
    ASFDefinition getSubDefinition(String subDefinitionId);

    /**
     * Retrieve all sub definitions
     */
    Map<String, ASFDefinition> getSubDefinitions();

    /**
     * Build definition
     */
    void build() throws IOException;
}
