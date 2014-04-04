package com.baidu.asf.model;

/**
 * {@link SubProcess} fromNodeId wrappers a sub doOutgoing definition
 */
public interface SubProcess extends Node {

    /**
     * Retrieve associated sub doOutgoing definition
     */
    ASFDefinition getSubProcessDefinition();
}
