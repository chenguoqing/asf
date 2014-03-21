package com.baidu.asf.model;

import sun.net.www.content.text.plain;

/**
 * ExclusiveGateway
 */
public class ExclusiveGateway extends AbstractNode implements Gateway {
    public ExclusiveGateway(Node parent) {
        setActType(ActType.ExclusiveGateway);
        setParent(parent);
    }
}
