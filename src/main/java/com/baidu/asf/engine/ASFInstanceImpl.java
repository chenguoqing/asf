package com.baidu.asf.engine;

import com.baidu.asf.ASFException;
import com.baidu.asf.engine.command.CommandExecutor;
import com.baidu.asf.engine.command.CompleteCommand;
import com.baidu.asf.engine.command.GetExecutionPathCommand;
import com.baidu.asf.engine.processor.ExecutionProcessor;
import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.model.ActType;
import com.baidu.asf.model.Node;
import com.baidu.asf.model.UserTask;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.enitity.ExecutionEntity;
import com.baidu.asf.persistence.enitity.InstanceEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link ASFInstance)
 */
public class ASFInstanceImpl extends AbstractVariableContext implements ASFInstance {

    private static final Map<ActType, ExecutionProcessor> processors = new HashMap<ActType, ExecutionProcessor>();
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
    /**
     * Process status
     */
    private ASFStatus status;

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
        return status;
    }


    @Override
    public List<ExecutionTask> getTasks() {
        List<ExecutionEntity> entities = entityManager.findExecutions(id);

        List<ExecutionTask> tasks = new ArrayList<ExecutionTask>();

        if (entities != null) {

            for (ExecutionEntity entity : entities) {
                UserTask userTask = findActElement(entity);

                ExecutionTaskImpl executionTask = new ExecutionTaskImpl(entity.getId(), userTask.getId(),
                        userTask.getName(), userTask.getType(), this);
                executionTask.setDescription(userTask.getDescription());

                tasks.add(executionTask);
            }
        }
        return tasks;
    }

    private <T extends Node> T findActElement(ExecutionEntity entity) {
        return (T) definition.findNode(entity.getActFullPath());
    }

    @Override
    public ExecutionPathNode getExecutionPath() {
        ProcessorContextImpl context = new ProcessorContextImpl(definition, this, entityManager, null);
        return executor.execute(context, new GetExecutionPathCommand());
    }

    @Override
    public void complete(long executionTaskId) {
        complete(executionTaskId, null);
    }

    @Override
    public void complete(long executionTaskId, Map<String, Object> variables) {
        if (status != ASFStatus.ACTIVE) {
            throw new ASFStatusException("Can't flow a non-active doOutgoing.");
        }
        // save variables
        setVariables(variables);

        ExecutionEntity executionEntity = entityManager.loadExecution(executionTaskId);
        UserTask node = findActElement(executionEntity);

        ASFDefinition def = definition.getSubDefinition(executionEntity.getActFullPath());

        if (def == null) {
            throw new ASFException("Not found the process definition :" + executionEntity.getActFullPath());
        }

        ProcessorContextImpl context = new ProcessorContextImpl(def, this, entityManager, node);
        context.setExecutionTaskId(executionTaskId);

        executor.execute(context, new CompleteCommand(node));
    }

    @Override
    public void pause() {
        if (status != ASFStatus.ACTIVE) {
            throw new ASFStatusException("Can't pause a non-active doOutgoing.");
        }
        this.status = ASFStatus.SUSPEND;
        instanceEntity.setStatus(ASFStatus.SUSPEND.value);
        entityManager.updateASFInstanceStatus(instanceEntity);
    }

    @Override
    public void resume() {
        if (status != ASFStatus.ACTIVE) {
            throw new ASFStatusException("Can't resume a non-suspend doOutgoing.");
        }
        instanceEntity.setStatus(ASFStatus.ACTIVE.value);
        this.status = ASFStatus.ACTIVE;
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
