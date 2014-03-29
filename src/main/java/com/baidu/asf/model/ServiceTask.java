package com.baidu.asf.model;

/**
 * Service task
 */
public class ServiceTask extends AbstractNode implements Task {
    public ServiceTask() {
        setActType(ActType.ServiceTask);
    }

    public ServiceTask(Node parent) {
        setActType(ActType.ServiceTask);
        setParent(parent);
    }
}
