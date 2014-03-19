package com.baidu.fsm.storage;

/**
 * Persist interface for state machine persistence
 */
public interface StatePersist {

    /**
     * Create a state machine instance, it will save the instance to persist storage.
     *
     * @return If success, the id will be retrieved.
     */
    void createStateMachineInstance(StateMachineEntity entity);

    /**
     * Retrieve state machine entity from persist storage by id
     */
    StateMachineEntity findStateMachineInstance(long id);

    /**
     * Persist the transition to storage, if the method is invoked multiply by same thread, the first time invocation should be
     * executed within external transaction; others invocations must be executed within new transaction.
     */
    int updateTransition(StateMachineEntity entity);

    /**
     * Persist all variables to storage.
     */
    int updateVariables(StateMachineEntity entity);

    /**
     * Save a sub state machine id to storage
     */
    int saveSubMachine(StateMachineEntity entity);

    /**
     * Remove the state machine info from persistence,the implementations may do "fake" delete,
     * as long as the {@link #findStateMachineInstance(long)} return null.
     *
     * @param id
     * @param version MVCC version
     * @return update record count
     */
    int removeStateMachine(long id, int version);
}
