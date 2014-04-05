package com.baidu.asf.model;

/**
 * StartEvent
 */
public class StartEvent extends AbstractNode implements Event {
    public StartEvent() {
        setActType(NodeType.StartEvent);
    }

    public StartEvent(Node parent) {
        setActType(NodeType.StartEvent);
        setParent(parent);
    }
}
