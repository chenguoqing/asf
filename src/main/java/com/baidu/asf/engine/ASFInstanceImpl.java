package com.baidu.asf.engine;

import com.baidu.asf.engine.command.Command;
import com.baidu.asf.engine.command.CommandExecutor;
import com.baidu.asf.engine.command.CompleteCommand;
import com.baidu.asf.engine.command.GetExecutionPathCommand;
import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.model.UserTask;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.EntityNotFoundException;
import com.baidu.asf.persistence.MVCCException;
import com.baidu.asf.persistence.enitity.ExecutionEntity;
import com.baidu.asf.persistence.enitity.InstanceEntity;

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

    private final CommandExecutor executor;

    public ASFInstanceImpl(long id, ASFDefinition definition, EntityManager entityManager, CommandExecutor executor) {
        this.id = id;
        this.definition = definition;
        this.entityManager = entityManager;
        this.executor = executor;
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
    public ExecutionPathNode getExecutionPath() {
        ProcessorContextImpl context = new ProcessorContextImpl();
        context.setDefinition(definition);
        context.setInstance(this);
        context.setEntityManager(entityManager);
        return executor.execute(context, new GetExecutionPathCommand());
    }

    @Override
    public void complete(long executionTaskId) {
        complete(executionTaskId, null);
    }

    @Override
    public void complete(long executionTaskId, Map<String, Object> variables) {
        if (instanceEntity.getStatus() != ASFStatus.ACTIVE.value) {
            throw new ASFStatusException("Can't flow a non-active doOutgoing.");
        }
        // save variables
        setVariables(variables);

        ExecutionEntity executionEntity;

        try {
            executionEntity = entityManager.loadExecution(executionTaskId);
        } catch (EntityNotFoundException e) {
            throw new ASFConcurrentModificationException(this, "Not found the user task id:" + e.id);
        }

        UserTask userTask = definition.findNode(executionEntity.getNodeFullId());

        ProcessorContextImpl context = new ProcessorContextImpl();
        context.setDefinition(userTask.getDefinition());
        context.setInstance(this);
        context.setEntityManager(entityManager);
        context.setExecutionTaskId(executionTaskId);

        executor.execute(context, new CompleteCommand(userTask));
    }

    @Override
    public void pause() {
        if (instanceEntity.getStatus() != ASFStatus.ACTIVE.value) {
            throw new ASFStatusException("Can't pause a non-active doOutgoing.");
        }

        setStatus(ASFStatus.SUSPEND);
    }

    @Override
    public void resume() {
        if (instanceEntity.getStatus() != ASFStatus.ACTIVE.value) {
            throw new ASFStatusException("Can't resume a non-suspend doOutgoing.");
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
        try {
            executor.execute(context, new Command<Void>() {
                @Override
                public Void execute(ProcessorContext context) {
                    entityManager.updateASFInstanceStatus(instanceEntity);
                    return null;
                }
            });
        } catch (MVCCException e) {
            throw new ASFConcurrentModificationException(this, "Failed to update status to " + instanceEntity.getStatus());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected CommandExecutor getExecutor() {
        return executor;
    }

    @Override
    protected ASFInstance getInstance() {
        return this;
    }
}
