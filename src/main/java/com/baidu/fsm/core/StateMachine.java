package com.baidu.fsm.core;

import com.baidu.fsm.core.graph.StateGraphDefinition;
import com.baidu.fsm.storage.StatePersist;

import java.io.Serializable;
import java.util.Map;

/**
 * The StateMachine interface
 *
 * @author chenguoqing01
 */
public interface StateMachine {

    /**
     * Retrieval state machine id, the id should be generated after has been persisted.
     */
    long getId();

    /**
     * Set the parent id
     */
    void setParentId(long parentId);

    /**
     * Retrieve the parent id
     */
    long getParentId();

    /**
     * Save a variable to state machine instance and the <tt>value</tt> should be implementation of {@link
     * java.io.Serializable}.
     */
    void setVariable(String name, Serializable value);

    /**
     * Retrieve the variable value by stateMachineName
     */
    Object getVariable(String name);

    /**
     * Retrieve all variables associated with state machine instance
     */
    Map<String, Serializable> getVariables();

    /**
     * Retrieve the sub state machine id by <tt>stateMachineName</tt>
     *
     * @param name sub state machine stateMachineName
     * @return sub state machine's id
     */
    long getSubMachine(String name);

    /**
     * Retrieve all id of sub machines
     */
    Map<String, Long> getSubMachines();

    /**
     * fromStart state transition, this will fire the persistence of state machine
     */
    void start();

    /**
     * Retrieve the current state
     */
    State getState();

    /**
     * Transit the current state to target state by  <tt>event</tt>, this will fire the persistence of transition
     * table. If <tt>event</tt> is null and there only one successor state, it will take it;otherwise,
     * the {@link com.baidu.fsm.core.StateMachineException} will be thrown.
     *
     * @param event transition event
     */
    void transit(String event);

    /**
     * Return the transition table associated with state machine
     */
    Transitions getTransitions();

    /**
     * Return the associated state graph definition
     */
    StateGraphDefinition getDefinition();

    /**
     * Retrieve the state persist implementation
     */
    StatePersist getStatePersist();

    /**
     * Remove state machine info from persistence
     */
    void destroy();
}
