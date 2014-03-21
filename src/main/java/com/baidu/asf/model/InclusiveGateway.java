package com.baidu.asf.model;

/**
 * InclusiveGateway
 */
public class InclusiveGateway extends AbstractNode implements Gateway {
    public InclusiveGateway(Node parent) {
        setActType(ActType.InclusiveGateway);
        setParent(parent);
    }
}
