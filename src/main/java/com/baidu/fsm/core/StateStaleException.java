package com.baidu.fsm.core;

/**
 * This exception indicates the state is stale
 */
public class StateStaleException extends StateMachineException {

    public final String fromState;
    public final String toState;
    public final String event;
    public final int oldVersion;
    public final int newVersion;

    public StateStaleException(String fromState, String toState, String event, int oldVersion, int newVersion) {
        super();
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    @Override
    public String getMessage() {
        String msg = String.format("The update state is stale. From:%s, to: %s event: %s, oldVersion:%d, newVersion:%d", fromState, toState, event, oldVersion, newVersion);
        return msg;
    }
}
