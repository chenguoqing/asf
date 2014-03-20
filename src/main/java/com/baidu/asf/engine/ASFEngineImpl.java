package com.baidu.asf.engine;

import com.baidu.asf.ASFException;
import com.baidu.asf.engine.command.Command;
import com.baidu.asf.engine.command.CommandExecutor;
import com.baidu.asf.engine.processor.ExecutionProcessor;
import com.baidu.asf.engine.processor.ExecutionProcessorRegister;
import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.model.ActType;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.enitity.InstanceEntity;

import java.util.Map;

/**
 * Default implementation of engine
 */
public class ASFEngineImpl implements ASFEngine {
    private EntityManager entityManager;
    private CommandExecutor executor;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public ASFInstance startASFInstance(final Class<? extends ASFDefinition> definitionClass) {
        return startASFInstance(definitionClass, null);
    }

    @Override
    public ASFInstance startASFInstance(final Class<? extends ASFDefinition> definitionClass, final Map<String,
            Object> variables) {
        final ASFDefinition definition;
        try {
            definition = definitionClass.newInstance();
        } catch (Exception e) {
            throw new ASFException(e);
        }
        ProcessorContextImpl context = new ProcessorContextImpl(definition, null, entityManager, null);
        return executor.execute(context, new Command<ASFInstance>() {
            @Override
            public ASFInstance execute(ProcessorContext context) {
                InstanceEntity entity = new InstanceEntity();
                entity.setDefName(definition.getName());
                entity.setDefName(definitionClass.getName());
                entity.setDefVersion(definition.getVersion());
                entity.setStatus(ASFInstance.ASFStatus.ACTIVE.value);
                // create instance
                entityManager.createASFInstance(entity);

                // start process
                ExecutionProcessor processor = ExecutionProcessorRegister.getProcessor(ActType.StartEvent);
                processor.doOutgoing(context);

                ASFInstance instance = new ASFInstanceImpl(entity.getId(), definition, entityManager, executor);
                instance.setVariables(variables);
                return instance;
            }
        });
    }

    @Override
    public ASFInstance findASFInstance(final long id) {

        ProcessorContextImpl context = new ProcessorContextImpl(null, null, entityManager, null);

        return executor.execute(context, new Command<ASFInstance>() {
            @Override
            public ASFInstance execute(ProcessorContext context) {
                InstanceEntity entity = entityManager.loadASFInstance(id);

                ASFDefinition definition;
                try {
                    definition = newDefinition(entity.getDefClassName());
                } catch (ASFException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ASFException(e);
                }

                if (definition.getVersion() != entity.getDefVersion()) {
                    throw new ASFException("Incompatible definition version.");
                }
                ASFInstance instance = new ASFInstanceImpl(entity.getId(), definition, entityManager, executor);
                return instance;
            }
        });
    }

    private ASFDefinition newDefinition(String name) throws Exception {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Class<?> cz = null;
        if (cl != null) {
            cz = cl.loadClass(name);
        }

        if (cz == null) {
            cz = ASFEngineImpl.class.getClassLoader().loadClass(name);
        }

        if (!ASFDefinition.class.isAssignableFrom(cz)) {
            throw new ASFException(name + " is not a implementation of " + ASFDefinition.class.getName());
        }

        return (ASFDefinition) cz.newInstance();
    }
}
