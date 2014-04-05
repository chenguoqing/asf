package com.baidu.asf.model;

/**
 * Service task
 */
public class ServiceTask extends AbstractNode implements Task {
    public ServiceTask() {
        setActType(NodeType.ServiceTask);
    }

    public ServiceTask(Node parent) {
        setActType(NodeType.ServiceTask);
        setParent(parent);
    }
}
