package com.baidu.asf.engine;

import com.baidu.asf.ASFException;
import com.baidu.asf.engine.processor.ExecutionProcessor;
import com.baidu.asf.engine.processor.ExecutionProcessorRegister;
import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.UserTask;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.enitity.ExecutionEntity;
import com.baidu.asf.persistence.enitity.InstanceEntity;
import com.baidu.asf.persistence.enitity.TransitionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link ASFInstance)
 */
public class ASFInstanceImpl extends AbstractVariableContext implements ASFInstance {

    /**
     * Unique id
     */
    private final long id;
    /**
     * Process definition
     */
    private final ASFDefinition definition;
    /**
     * Entity manage instance
     */
    private final EntityManager entityManager;
    /**
     * Associated instance entity
     */
    private final InstanceEntity instanceEntity;

    public ASFInstanceImpl(long id, ASFDefinition definition, EntityManager entityManager) {
        this.id = id;
        this.definition = definition;
        this.entityManager = entityManager;
        this.instanceEntity = entityManager.loadASFInstance(id);
    }

    @Override
    public ASFDefinition getDefinition() {
        return definition;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public ASFStatus getStatus() {
        return ASFStatus.get(instanceEntity.getStatus());
    }

    @Override
    public List<ExecutionTask> getTasks() {
        List<ExecutionEntity> entities = entityManager.findExecutions(id);

        List<ExecutionTask> tasks = new ArrayList<ExecutionTask>();

        if (entities != null) {

            for (ExecutionEntity entity : entities) {
                UserTask userTask = definition.findNode(entity.getNodeFullId());

                ExecutionTaskImpl executionTask = new ExecutionTaskImpl(entity.getId(), userTask, this);
                tasks.add(executionTask);
            }
        }
        return tasks;
    }

    @Override
    public List<ExecutionPath> getExecutionPath() {

        List<TransitionEntity> transitionEntities = entityManager.findTransitions(getId());

        List<ExecutionPath> executionPaths = new ArrayList<ExecutionPath>();
        if (transitionEntities == null) {
            return executionPaths;
        }

        for (TransitionEntity entity : transitionEntities) {
            executionPaths.add(new ExecutionPath(entity.getSourceRef(), entity.getTargetRef(), entity.isVirtualFlow()));
        }

        return executionPaths;
    }

    @Override
    public void complete(long executionTaskId) {
        complete(executionTaskId, null);
    }

    @Override
    public void complete(long executionTaskId, Map<String, Object> variables) {
        if (instanceEntity.getStatus() != ASFStatus.ACTIVE.value) {
            throw new ASFException("Can't flow a non-active doOutgoing.");
        }
        // save variables
        setVariables(variables);

        ExecutionEntity executionEntity = entityManager.loadExecution(executionTaskId);

        UserTask userTask = definition.findNode(executionEntity.getNodeFullId());

        ProcessorContextImpl context = new ProcessorContextImpl();
        context.setDefinition(userTask.getDefinition());
        context.setInstance(this);
        context.setEntityManager(entityManager);
        context.setExecutionTaskId(executionTaskId);

        ExecutionProcessor processor = ExecutionProcessorRegister.getProcessor(ActType.UserTask);
        processor.doOutgoing(context, userTask);
    }

    @Override
    public void pause() {
        if (instanceEntity.getStatus() != ASFStatus.ACTIVE.value) {
            throw new ASFException("Can't pause a non-active doOutgoing.");
        }

        setStatus(ASFStatus.SUSPEND);
    }

    @Override
    public void resume() {
        if (instanceEntity.getStatus() != ASFStatus.ACTIVE.value) {
            throw new ASFException("Can't resume a non-suspend doOutgoing.");
        }
        setStatus(ASFStatus.ACTIVE);
    }

    /**
     * Update the instance status with transaction,the status validation should be made before invocation
     */
    public void setStatus(ASFStatus status) {
        instanceEntity.setStatus(status.value);
        ProcessorContextImpl context = new ProcessorContextImpl();
        context.setDefinition(definition);
        context.setInstance(this);
        context.setEntityManager(entityManager);
        entityManager.updateASFInstanceStatus(instanceEntity);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected ASFInstance getInstance() {
        return this;
    }
}
