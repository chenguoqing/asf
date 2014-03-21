package com.baidu.asf.model;

/**
 * User task
 */
public class UserTask extends AbstractNode implements Task {
    public UserTask(Node parent) {
        setActType(ActType.UserTask);
        setParent(parent);
    }
}
