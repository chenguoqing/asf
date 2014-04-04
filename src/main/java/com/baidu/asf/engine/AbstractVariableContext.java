package com.baidu.asf.engine;

import com.baidu.asf.persistence.DuplicateKeyException;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.MVCCException;
import com.baidu.asf.persistence.enitity.VariableEntity;

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
        return getVariable(name, VariableEntity.VariableClass.USER);
    }

    @Override
    public void removeVariable(final String name) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();
        entityManager.removeVariable(id, name, VariableEntity.VariableClass.USER);
    }

    @Override
    public void clearVariables() {
        clearVariables(VariableEntity.VariableClass.USER);
    }

    public void clearVariables(final VariableEntity.VariableClass variableClass) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();

        entityManager.clearVariables(id, variableClass);
    }

    @Override
    public void setSystemVariable(String name, Object value) {
        setVariable(name, value, VariableEntity.VariableClass.SYSTEM);
    }

    private VariableEntity createVariable(final String name, final Object value, final VariableEntity.VariableClass variableClass) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();

        VariableEntity variable = new VariableEntity(id, name, value, variableClass);
        entityManager.createVariable(variable);
        return variable;
    }

    private void setVariable(final String name, final Object value, final VariableEntity.VariableClass variableClass) {
        final EntityManager entityManager = getEntityManager();

        VariableEntity entity = getEntityManager().findVariable(getInstance().getId(), name,
                variableClass);

        if (entity == null) {
            createVariable(name, value, variableClass);
        } else {
            entity.setVariable(name, value);
            entityManager.updateVariable(entity);
        }
    }

    @Override
    public Object getSystemVariable(String name) {
        return getVariable(name, VariableEntity.VariableClass.SYSTEM);
    }

    private Object getVariable(final String name, final VariableEntity.VariableClass variableClass) {
        final EntityManager entityManager = getEntityManager();
        VariableEntity entity = entityManager.findVariable(getInstance().getId(), name, variableClass);
        return entity == null ? null : entity.getValue();
    }

    @Override
    public void removeSystemVariable(String name) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();
        entityManager.removeVariable(id, name, VariableEntity.VariableClass.SYSTEM);
    }

    @Override
    public void clearSystemVariables() {
        clearVariables(VariableEntity.VariableClass.SYSTEM);
    }

    @Override
    public int incrementAndGet(final String name) {
        return doIncrementAndGet(name);
    }

    private int doIncrementAndGet(String name) {
        final EntityManager entityManager = getEntityManager();
        final long id = getInstance().getId();
        try {
            VariableEntity entity = entityManager.findVariable(id, name, VariableEntity.VariableClass.SYSTEM);

            if (entity == null) {
                createVariable(name, 1, VariableEntity.VariableClass.SYSTEM);
                return 1;
            }

            int value = entity.getLong().intValue() + 1;

            // increment value
            entity.setVariable(name, value);

            // update persist
            entityManager.updateVariable(entity);

            return value;
        } catch (RuntimeException e) {
            if (e instanceof DuplicateKeyException || e instanceof MVCCException) {
                return doIncrementAndGet(name);
            }
            throw e;
        }
    }

    protected abstract ASFInstance getInstance();

    protected abstract EntityManager getEntityManager();
}
