package com.baidu.asf.engine;

import com.baidu.asf.ASFException;
import com.baidu.asf.engine.processor.ExecutionProcessor;
import com.baidu.asf.engine.processor.ExecutionProcessorRegister;
import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.model.NodeType;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.enitity.InstanceEntity;

import java.util.Map;

/**
 * Default implementation of engine
 */
public class ASFEngineImpl implements ASFEngine {
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ASFInstance startASFInstance(ASFDefinition definition) {
        return startASFInstance(definition, null);
    }

    @Override
    public ASFInstance startASFInstance(final ASFDefinition definition, final Map<String,
            Object> variables) {

        InstanceEntity entity = new InstanceEntity();
        entity.setDefId(definition.getId());
        entity.setDefVersion(definition.getVersion());
        entity.setStatus(ASFInstance.ASFStatus.ACTIVE.value);
        // create instance
        entityManager.createASFInstance(entity);

        ASFInstance instance = new ASFInstanceImpl(entity.getId(), definition, entityManager);
        instance.setVariables(variables);

        final ProcessorContextImpl context = new ProcessorContextImpl();
        context.setEntityManager(entityManager);
        context.setDefinition(definition);
        context.setInstance(instance);

        // start process
        ExecutionProcessor processor = ExecutionProcessorRegister.getProcessor(NodeType.StartEvent);
        processor.doOutgoing(context, definition.getStartEvent());

        return instance;
    }

    @Override
    public ASFInstance findASFInstance(final ASFDefinition definition, final long id) {

        ProcessorContextImpl context = new ProcessorContextImpl();
        context.setEntityManager(entityManager);

        InstanceEntity entity = entityManager.loadASFInstance(id);

        if (definition.getVersion() != entity.getDefVersion()) {
            throw new ASFException("Incompatible definition version.");
        }

        return new ASFInstanceImpl(entity.getId(), definition, entityManager);
    }
}
