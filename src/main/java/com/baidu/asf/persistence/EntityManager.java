package com.baidu.asf.persistence;

import com.baidu.asf.persistence.enitity.ExecutionEntity;
import com.baidu.asf.persistence.enitity.InstanceEntity;
import com.baidu.asf.persistence.enitity.TransitionEntity;
import com.baidu.asf.persistence.enitity.VariableEntity;

import java.util.List;

/**
 * The entity persistence interface
 */
public interface EntityManager {
    /**
     * Create a doOutgoing instance,if success the new entity id will be set to {@link sun.security.jca.GetInstance
     * .Instance.setId()}
     *
     * @throws ASFPersistenceException any persistence error
     */
    void createASFInstance(InstanceEntity instanceEntity);

    /**
     * Load instance entity by id, the {@link com.baidu.asf.persistence.EntityNotFoundException} will be raised if
     * the entity not found.
     */
    InstanceEntity loadASFInstance(long id);

    /**
     * Retrieve all active instance
     */
    List<InstanceEntity> findASFInstances();

    /**
     * Update the status of doOutgoing instance
     *
     * @throws ASFPersistenceException                 any persistence error
     * @throws com.baidu.asf.persistence.MVCCException if the version conflicted
     */
    void updateASFInstanceStatus(InstanceEntity instanceEntity);

    /**
     * Create a execution entity, the new execution id will be set to {@link com.baidu.asf.persistence.enitity.ExecutionEntity} if success
     *
     * @throws ASFPersistenceException any persistence error
     */
    void createExecution(ExecutionEntity executionEntity);

    /**
     * Load execution by id
     *
     * @throws {@link com.baidu.asf.persistence.EntityNotFoundException} if the entity is not found
     */
    ExecutionEntity loadExecution(long id);

    /**
     * Remove the execution entity by id
     *
     * @throws ASFPersistenceException                 any persistence error
     * @throws com.baidu.asf.persistence.MVCCException if the version conflicted
     */
    void removeExecution(long id);

    /**
     * Remove all executions associated with instance
     */
    void removeExecutions(long instanceId);

    /**
     * Retrieve all active executions of instance <tt>instanceId</tt>
     */
    List<ExecutionEntity> findExecutions(long instanceId);

    /**
     * Create transition entity, the new transition id will be set to {@link com.baidu.asf.persistence.enitity.TransitionEntity} if success
     *
     * @throws ASFPersistenceException any persistence error
     */
    void createTransition(TransitionEntity transitionEntity);

    /**
     * Retrieve all transition path for instance
     */
    List<TransitionEntity> findTransitions(long instanceId);

    /**
     * Create a variable entity, this will not be protected by MVCC
     *
     * @throws ASFPersistenceException any persistence error
     */
    void createVariable(VariableEntity variable);

    /**
     * Load variable entity, if the variable is not exists,the {@link com.baidu.asf.persistence
     * .EntityNotFoundException} will be thrown.
     */
    VariableEntity loadVariable(long instanceId, String name, VariableEntity.VariableClass variableClass);

    /**
     * Load variable entity, if the variable is not exists, null will be returned.
     */
    VariableEntity findVariable(long instanceId, String name, VariableEntity.VariableClass variableClass);

    /**
     * Update the variable to new value if the variable is exist,otherwise, create it.
     *
     * @throws ASFPersistenceException any persistence error
     */
    void updateVariable(VariableEntity variable);

    /**
     * Remove the variable from doOutgoing instance identified by <tt>instanceId</tt>
     */
    void removeVariable(long instanceId, String name, VariableEntity.VariableClass variableClass);

    /**
     * Remove all variables associated with doOutgoing instance <tt>instanceId</tt>
     */
    void clearVariables(long instanceId, VariableEntity.VariableClass variableClass);

    /**
     * Retrieve all variables associated with <tt>instanceId</tt>
     */
    List<VariableEntity> findVariables(long instanceId);
}
