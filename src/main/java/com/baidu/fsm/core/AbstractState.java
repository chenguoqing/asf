package com.baidu.fsm.core;

import java.util.*;

/**
 * Default implementation of {@link com.baidu.fsm.core.State} interface
 */
public abstract class AbstractState implements State {

    public final String name;

    private final Map<String, State> successors = new HashMap<String, State>();

    private final List<StateListener> listeners = new ArrayList<StateListener>();

    public AbstractState(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * The implements is performance low, but is should not be invoked frequently.
     */
    @Override
    public boolean hasSuccessor(String stateName) {
        if (stateName == null) {
            throw new IllegalArgumentException("stateName is null.");
        }

        for (State successor : successors.values()) {
            if (successor.getName().equals(stateName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasSuccessor(String event, String stateName) {

        if (event == null || stateName == null) {
            throw new IllegalArgumentException("event or stateName is null.");
        }

        State successor = successors.get(event);
        return successor == null ? false : successor.getName().equals(stateName);
    }

    /**
     * Add the successor to current state
     */
    @Override
    public void addSuccessor(String event, State successor) {
        successors.put(event, successor);
    }

    int successorSize() {
        return successors.size();
    }

    @Override
    public State getSuccessor(String event) {
        return successors.get(event);
    }

    @Override
    public Map<String, State> getSuccessors() {
        return new HashMap<String, State>(successors);
    }

    @Override
    public void addStateListener(StateListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public List<StateListener> getStateListeners() {
        return Collections.unmodifiableList(listeners);
    }

    @Override
    public void onState(State from, String event, StateMachine stateMachine) {

        State state = stateMachine.getDefinition().getState(from.getName());

        StateEventSource eventSource;
        // the event come from same state machine
        if (state == from) {
            eventSource = new StateEventSource(stateMachine.getId(), from, event);
        } else {

        }

        final StateEvent stateEvent = null;//new StateEvent(stateMachine, from, this, event);

        for (StateListener listener : getStateListeners()) {
            listener.stateChange(stateEvent);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        }

        return ((State) obj).getName().equals(name);
    }

    @Override
    public String toString() {
        return String.format("{State:%s,stateType:%s}", name, getType());
    }
}
