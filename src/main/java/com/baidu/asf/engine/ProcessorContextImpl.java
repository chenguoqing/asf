package com.baidu.asf.engine;

import com.baidu.asf.engine.command.CommandExecutor;
import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.model.Node;
import com.baidu.asf.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation
 */
public class ProcessorContextImpl implements ProcessorContext {

    private final Map<String, Object> params = new HashMap<String, Object>();
    private ASFInstance instance;
    private ASFDefinition definition;
    private EntityManager entityManager;
    private long executionTaskId;
    private Node node;
    private CommandExecutor executor;

    public ProcessorContextImpl(ASFInstance instance, EntityManager entityManager, Node node) {
        this(instance.getDefinition(), instance, entityManager, node);
    }

    public ProcessorContextImpl(ASFDefinition definition, ASFInstance instance, EntityManager entityManager,
                                Node node) {
        this.definition = definition == null ? instance.getDefinition() : definition;
        this.instance = instance;
        this.entityManager = entityManager;
        this.node = node;
    }

    @Override
    public ASFInstance getInstance() {
        return instance;
    }

    @Override
    public ASFDefinition getDefinition() {
        return definition;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setExecutionTaskId(long executionTaskId) {
        this.executionTaskId = executionTaskId;
    }

    @Override
    public long getExecutionTaskId() {
        return executionTaskId;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public void addParam(String name, Object value) {
        params.put(name, value);
    }

    public void addParams(Map<String, Object> params) {
        if (params != null) {
            this.params.putAll(params);
        }
    }

    public <T> T getParam(String name) {
        return (T) params.get(name);
    }

    @Override
    public Map<String, Object> getParams() {
        return new HashMap<String, Object>(params);
    }

    @Override
    public ProcessorContext newContext(ASFDefinition definition, Node node) {
        ProcessorContextImpl context = new ProcessorContextImpl(definition, instance,
                entityManager, node);
        context.params.putAll(params);
        context.executor = executor;
        return context;
    }

    @Override
    public CommandExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }
}
