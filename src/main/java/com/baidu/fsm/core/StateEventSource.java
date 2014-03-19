package com.baidu.fsm.core;

/**
 * {@link com.baidu.fsm.core.StateEventSource} describes a source of state event,it contains source state machine,
 * source state and source event.
 */
public class StateEventSource {
    public final long fromStateMachine;
    public final State fromState;
    public final String fromEvent;

    public StateEventSource(long fromStateMachine, State fromState, String fromEvent) {
        this.fromStateMachine = fromStateMachine;
        this.fromState = fromState;
        this.fromEvent = fromEvent;
    }
}
