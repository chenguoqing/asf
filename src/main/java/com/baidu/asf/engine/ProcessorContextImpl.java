package com.baidu.asf.engine;

import com.baidu.asf.model.ASFDefinition;
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

    public void setInstance(ASFInstance instance) {
        this.instance = instance;
    }

    public void setDefinition(ASFDefinition definition) {
        this.definition = definition;
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
}
