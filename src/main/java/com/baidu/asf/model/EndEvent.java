package com.baidu.asf.model;

/**
 * End event
 */
public class EndEvent extends AbstractNode implements Event {
    public EndEvent(Node parent) {
        setActType(ActType.EndEvent);
        setParent(parent);
    }
}
