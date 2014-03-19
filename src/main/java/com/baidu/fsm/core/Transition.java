package com.baidu.fsm.core;

/**
 * Transition entry
 */
public class Transition {
    /**
     * Target state
     */
    public final String toState;
    /**
     * Source state
     */
    public final String fromState;
    /**
     * event
     */
    public final String event;
    /**
     * Transition date
     */
    public final long date;

    public Transition(String toState, String fromState, String event) {
        this(toState, fromState, event, System.currentTimeMillis());
    }

    public Transition(String toState, String fromState, String event, long date) {
        this.toState = toState;
        this.fromState = fromState;
        this.event = event;
        this.date = date;
    }
}
