package com.baidu.fsm.core;

import java.util.Stack;

/**
 * State transition table
 */
public interface Transitions {
    /**
     * Retrieve the transition stack newContext
     */
    Stack<Transition> getTransitionStack();

    /**
     * Add transition to table
     */
    void addTransition(Transition transition);

    /**
     * Serialize the transitions to byte array
     */
    byte[] serialize();
}
