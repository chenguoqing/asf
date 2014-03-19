package com.baidu.fsm.core;

import java.util.List;
import java.util.Map;

/**
 * This class represents a concrete <tt>state</tt> concept
 */
public interface State {

    /**
     * State stateType
     */
    enum StateType {
        /**
         * Start state
         */
        Start,
        /**
         * End state
         */
        End,
        /**
         * Wait nodeId, the state remains on this state until the manual transition
         */
        Wait,
        /**
         * ExclusiveGateway nodeId,the state will transit to nodeId state by decision
         */
        ExclusiveGateway,
        /**
         * Parallel gateway, the state will fork or join with parallel actions.
         */
        ParallelGateway
    }

    /**
     * Retrieve the unique state stateMachineName
     */
    String getName();

    /**
     * Retrieve the state stateType
     */
    StateType getType();

    /**
     * Whether exists the nodeId state identified by <code>stateName</code>
     */
    boolean hasSuccessor(String stateName);

    /**
     * Whether this nodeId has one successor state represented by <code>stateName</code> by event <code>event</code>
     *
     * @param event     the follow event stateMachineName
     * @param stateName the successor state stateMachineName
     */
    boolean hasSuccessor(String event, String stateName);

    /**
     * Add successor state
     */
    void addSuccessor(String event, State successor);

    /**
     * Has event?
     */
    State getSuccessor(String event);

    /**
     * Retrieve all successors
     */
    Map<String, State> getSuccessors();

    /**
     * Add state listener instance to this state, when transit to this state, these registered listeners will be invoked by order.
     */
    void addStateListener(StateListener listener);

    /**
     * Return all registered listeners
     */
    List<StateListener> getStateListeners();

    /**
     * This method  will be called when state machine transits to current state
     *
     * @param from         from state
     * @param event        from event
     * @param stateMachine associated state machine with current state(this)
     */
    void onState(State from, String event, StateMachine stateMachine);
}
