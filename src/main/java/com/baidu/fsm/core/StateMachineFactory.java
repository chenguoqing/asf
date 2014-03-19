package com.baidu.fsm.core;

import com.baidu.fsm.core.graph.StateGraphDefinition;
import com.baidu.fsm.storage.StatePersist;

/**
 * The helper class for creating a state machine instance
 */
public class StateMachineFactory {

    /**
     * Create a new state machine instance by <tt>definitionClass</tt>, this instance will be persisted to storage by
     * <tt>persist</tt>
     *
     * @param persist persistence interface
     * @return state machine instance
     * @throws com.baidu.fsm.core.StateMachineException any exception
     */
    public static StateMachine createStateMachine(StateGraphDefinition definition, StatePersist persist) {
        return createStateMachine(null, definition, persist);
    }

    /**
     * Retrieve the state machine instance from storage by <tt>id</tt>
     *
     * @param id      state machine id, if null, it will create a new state machine
     * @param persist persistence interface
     * @return state machine instance
     * @throws com.baidu.fsm.core.StateMachineException any exception
     */
    public static StateMachine createStateMachine(Long id, StateGraphDefinition definition,
                                                  StatePersist persist) {
        if (definition == null || persist == null) {
            throw new IllegalArgumentException("definition parameter must not be null.");
        }

        DefaultStateMachine stateMachine = new DefaultStateMachine();
        stateMachine.setId(id);
        stateMachine.setDefinition(definition);
        stateMachine.setPersist(persist);
        stateMachine.initialize();
        return stateMachine;
    }
}
