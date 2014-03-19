package com.baidu.fsm.core;

/**
 * The interface for detecting transition event for auto state
 */
public interface EventDecision {

    /**
     * Return the transition event for nodeId event
     *
     * @param stateMachine the state machine instance associated with current state(associated with {@link com.baidu.fsm.core.EventDecision}
     * @param eventSources the event source
     */
    String decideEvent(StateMachine stateMachine, StateEventSource... eventSources);
}
