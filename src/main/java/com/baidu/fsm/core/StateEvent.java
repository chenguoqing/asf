package com.baidu.fsm.core;

import java.util.EventObject;

/**
 * This represents a state event
 */
public class StateEvent extends EventObject {
    public final State to;

    public StateEvent(StateEventSource source1, State to) {
        super(source1);
        this.to = to;
    }
}
