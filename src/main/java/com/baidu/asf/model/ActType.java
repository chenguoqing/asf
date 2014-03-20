package com.baidu.asf.model;

/**
 * All element types which can be defined on doOutgoing graph
 */
public enum ActType {
    Flow(0),
    Message(1),
    StartEvent(2),
    EndEvent(3),
    UserTask(4),
    ServiceTask(4),
    ParallelGateway(6),
    ExclusiveGateway(7),
    InclusiveGateway(8),
    SubProcess(9),
    Definition(10);

    public final int type;

    ActType(int type) {
        this.type = type;
    }

    public static ActType get(int type) {
        if (type < Flow.type || type >= values().length) {
            throw new IllegalArgumentException("Invalidate type:" + type);
        }

        return values()[type];
    }
}
