package com.baidu.fsm.spring;

import com.baidu.fsm.core.StateMachine;

/**
 * The convenient interface for retrieving state machine instance
 */
public interface StateMachineBuilder {

    /**
     * Create a new state machine instance
     */
    StateMachine createStateMachine();

    /**
     * Retrieve the state machine instance by <tt>id</tt>
     */
    StateMachine getStateMachine(long id);
}
