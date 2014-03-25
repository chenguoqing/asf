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
    public ASFInstance startASFInstance(ASFDefinition definition) {
        return startASFInstance(definition, null);
    }

    @Override
    public ASFInstance startASFInstance(final ASFDefinition definition, final Map<String,
            Object> variables) {

        definition.build();

        ProcessorContextImpl context = new ProcessorContextImpl(definition, null, entityManager, null);
        return executor.execute(context, new Command<ASFInstance>() {
            @Override
            public ASFInstance execute(ProcessorContext context) {
                InstanceEntity entity = new InstanceEntity();
                entity.setDefId(definition.getId());
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
    public ASFInstance findASFInstance(final ASFDefinition definition, final long id) {

        definition.build();

        ProcessorContextImpl context = new ProcessorContextImpl(null, null, entityManager, null);

        return executor.execute(context, new Command<ASFInstance>() {
            @Override
            public ASFInstance execute(ProcessorContext context) {
                InstanceEntity entity = entityManager.loadASFInstance(id);

                if (definition.getVersion() != entity.getDefVersion()) {
                    throw new ASFException("Incompatible definition version.");
                }
                ASFInstance instance = new ASFInstanceImpl(entity.getId(), definition, entityManager, executor);
                return instance;
            }
        });
    }
}
