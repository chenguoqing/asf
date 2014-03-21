package com.baidu.asf.model;

/**
 * StartEvent
 */
public class StartEvent extends AbstractNode implements Event {
    public StartEvent(Node parent) {
        setActType(ActType.StartEvent);
        setParent(parent);
    }
}
