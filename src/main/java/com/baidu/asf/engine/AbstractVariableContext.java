package com.baidu.asf.engine;

import com.baidu.asf.engine.command.Command;
import com.baidu.asf.engine.command.CommandExecutor;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.MVCCException;
import com.baidu.asf.persistence.enitity.VariableEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The default implementation of variable context(user variable and system variable)
 */
public abstract class AbstractVariableContext implements VariableContext, SystemVariableContext {
    @Override
    public void setVariable(String name, Object value) {
        setVariable(name, value, VariableEntity.VariableClass.USER);
    }

    @Override
    public void setVariables(Map<String, Object> variables) {
        if (variables == null) {
            return;
        }
        for (Iterator<String> it = variables.keySet().iterator(); it.hasNext(); ) {
            String name = it.next();
            Object value = variables.get(name);
            setVariable(name, value);
        }
    }

    @Override
    public Object getVariable(String name) {
        return getVariable(name, VariableEntity.VariableClass.USER, null);
    }

    @Override
    public void removeVariable(final String name) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();

        executeCommand(new Command<Object>() {
            @Override
            public Object execute(ProcessorContext context) {
                entityManager.removeVariable(id, name, VariableEntity.VariableClass.USER);
                return null;
            }
        });
    }

    private void removeVariable(final String name, final VariableEntity.VariableClass variableClass) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();

        executeCommand(new Command<Object>() {
            @Override
            public Object execute(ProcessorContext context) {
                entityManager.removeVariable(id, name, VariableEntity.VariableClass.USER);
                return null;
            }
        });
    }

    @Override
    public void setSystemVariable(String name, Object value) {
        setVariable(name, value, VariableEntity.VariableClass.SYSTEM);
    }

    private void setVariable(final String name, final Object value, final VariableEntity.VariableClass variableClass) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();

        executeCommand(new Command<Object>() {
            @Override
            public Object execute(ProcessorContext context) {
                VariableEntity variable = new VariableEntity(id, name, value, variableClass);
                entityManager.updateVariable(variable);
                return null;
            }
        });
    }

    @Override
    public Object getSystemVariable(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ProcessorContext.ParamKeys.DisabledCache.paramName, true);
        return getVariable(name, VariableEntity.VariableClass.SYSTEM, params);
    }

    private Object getVariable(final String name, final VariableEntity.VariableClass variableClass, Map<String,
            Object> params) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();
        return executeCommand(new Command<Object>() {
            @Override
            public Object execute(ProcessorContext context) {
                VariableEntity entity = entityManager.loadVariable(id, name, variableClass);
                return entity == null ? null : entity.getValue();
            }
        }, params);
    }

    @Override
    public void removeSystemVariable(String name) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();
        entityManager.removeVariable(id, name, VariableEntity.VariableClass.SYSTEM);
    }

    @Override
    public int incrementAndGet(final String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ProcessorContext.ParamKeys.DisabledCache.paramName, true);
        return executeCommand(new Command<Integer>() {
            @Override
            public Integer execute(ProcessorContext context) {
                return doIncrementAndGet(name);
            }
        }, params);
    }

    private int doIncrementAndGet(String name) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();
        VariableEntity entity = entityManager.findVariable(id, name, VariableEntity.VariableClass.SYSTEM);

        if (entity != null) {
            setVariable(name, 1, VariableEntity.VariableClass.SYSTEM);
            return 1;
        }

        int value = entity.getLong().intValue();

        try {
            setVariable(name, value + 1, VariableEntity.VariableClass.SYSTEM);
        } catch (MVCCException e) {
            return doIncrementAndGet(name);
        }
        return value;
    }

    private <T> T executeCommand(Command<T> command) {
        return executeCommand(command, null);
    }

    private <T> T executeCommand(Command<T> command, Map<String, Object> params) {
        return getExecutor().execute(createExecutionContext(params), command);
    }

    private ProcessorContext createExecutionContext(Map<String, Object> params) {
        ProcessorContextImpl context = new ProcessorContextImpl(getInstance(), getEntityManager(), null);
        if (params != null) {
            context.addParams(params);
        }
        return context;
    }

    protected abstract ASFInstance getInstance();

    protected abstract EntityManager getEntityManager();

    protected abstract CommandExecutor getExecutor();
}
