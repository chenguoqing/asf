package com.baidu.fsm.core;

import com.baidu.fsm.core.graph.StateGraphDefinition;
import com.baidu.fsm.storage.StateMachineEntity;
import com.baidu.fsm.storage.StatePersist;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Default implementation of {@link com.baidu.fsm.core.StateMachine}
 */
public class DefaultStateMachine implements StateMachine {

    /**
     * Transition table
     */
    private Transitions transitions;
    /**
     * Current state
     */
    private State currentState;
    /**
     * Unique state machine id
     */
    private Long id;

    /**
     * Associated entity object for storage
     */
    private StateMachineEntity entity;

    private StateGraphDefinition definition;
    private StatePersist persist;

    @Override
    public void setParentId(long parentId) {
        this.entity.setParentId(parentId);
    }

    @Override
    public long getParentId() {
        return entity.getParentId();
    }

    void setId(Long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setPersist(StatePersist persist) {
        this.persist = persist;
    }

    public void setDefinition(StateGraphDefinition definition) {
        this.definition = definition;
    }

    @Override
    public void setVariable(String name, Serializable value) {
        entity.setVariable(name, value);
        persist.updateVariables(entity);
    }

    @Override
    public Object getVariable(String name) {
        return entity.getVariable(name);
    }

    @Override
    public Map<String, Serializable> getVariables() {
        return entity.getVariables();
    }

    private void addSubMachine(String name, long id) {
        entity.addSubMachine(name, id);
    }

    @Override
    public long getSubMachine(String name) {
        return entity.getSubMachine(name);
    }

    @Override
    public Map<String, Long> getSubMachines() {
        return entity.getSubMachines();
    }

    /**
     * Build state machine instance from storage
     */
    void initialize() {

        // buildStateGraph and validate state graph definition
        definition.buildStateGraph();

        if (id == null) {
            createStateMachine();
        } else {
            buildStateMachine();
        }
    }

    @Override
    public void start() {

        if (!(currentState instanceof StartState)) {
            throw new StateMachineException("The state machine has been started.");
        }
        final StartState startState = (StartState) currentState;
        for (String event : startState.getSuccessors().keySet()) {
            transit(event);
            break;
        }
    }

    private void createStateMachine() {
        this.currentState = definition.getStartState();
        this.transitions = new DefaultTransitions();
        StatePersist statePersist = getStatePersist();

        this.entity = new StateMachineEntity();

        entity.setVersion(0);
        entity.setStateDefVersion(definition.getVersion());
        entity.setState(currentState.getName());
        statePersist.createStateMachineInstance(entity);
        this.id = entity.getId();
    }

    private void buildStateMachine() {
        // retrieve the persistence info from storage
        this.entity = getStatePersist().findStateMachineInstance(id);

        if (entity == null) {
            throw new StateMachineException("Not found the state machine by id:" + id);
        }

        // invalidate version
        if (entity.getStateDefVersion() != definition.getVersion()) {
            throw new StateMachineException(String.format("The state machine instance version:%d is incompatible " +
                    "with state definition version: %d\n"));
        }

        this.currentState = definition.getState(entity.getState());

        // validate the current state
        if (currentState == null) {
            String msg = String.format("Not found the state: %s,state machine: %s from stat graph definition.",
                    entity.getState(), definition.getName());
            throw new StateMachineException(msg);
        }

        try {
            this.transitions = new DefaultTransitions(entity.getTransitions());
        } catch (IOException e) {
            String msg = String.format("Can't deserialize the transition table, state machine id:%d", id);
            throw new StateMachineException(msg);
        }
    }

    @Override
    public State getState() {
        return currentState;
    }

    @Override
    public void transit(String event) {

        if (event == null) {

            if (currentState.getSuccessors().size() > 1) {
                throw new IllegalArgumentException("Parameter is null.");
            }

            event = currentState.getSuccessors().keySet().iterator().next();
        }

        State target = currentState.getSuccessor(event);

        if (target == null) {
            String s = String.format("Invalidate transition state:%s event:%s", currentState.getName(), event);
            throw new StateMachineException(s);
        }

        final State from = currentState;

        Transition entry = new Transition(target.getName(), currentState.getName(), event);
        transitions.addTransition(entry);

        final int oldVersion = entity.getVersion();

        StateMachineEntity existEntry = getStatePersist().findStateMachineInstance(id);

        if (existEntry == null) {
            throw new StateMachineException("Not found the state machine instance for id:" + id);
        }

        if (existEntry.getVersion() != oldVersion) {
            throw new StateStaleException(currentState.getName(), target.getName(), event, oldVersion, oldVersion);
        }

        entity.setState(target.getName());
        entity.setTransitions(transitions.serialize());
        int count = getStatePersist().updateTransition(entity);

        // Failed to update transition(e.g. state machine is transited)
        if (count == 0) {
            throw new StateStaleException(currentState.getName(), target.getName(), event, oldVersion, oldVersion);
        }
        this.currentState = target;

        fireStateEvent(from, currentState, event);
    }

    private void fireStateEvent(State from, State to, String event) {
        try {
            // fire the state event change
            to.onState(from, event, this);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StateStaleException) {
                throw (StateStaleException) e.getCause();
            }
            throw e;
        }
    }

    @Override
    public Transitions getTransitions() {
        return transitions;
    }

    @Override
    public StateGraphDefinition getDefinition() {
        return definition;
    }

    @Override
    public StatePersist getStatePersist() {
        return persist;
    }

    @Override
    public void destroy() {
        if (id == -1L) {
            return;
        }

        getStatePersist().removeStateMachine(id, entity.getVersion());
        reset();
    }

    private void reset() {
        this.id = -1L;
        this.entity = null;
        this.definition = null;
        this.currentState = null;
    }
}
