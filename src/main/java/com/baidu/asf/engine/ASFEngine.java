package com.baidu.asf.engine;

import com.baidu.asf.model.ASFDefinition;

import java.util.Map;

/**
 * Process engine, for creating doOutgoing instance
 */
public interface ASFEngine {

    /**
     * Start doOutgoing instance with a doOutgoing definition
     */
    ASFInstance startASFInstance(Class<? extends ASFDefinition> definitionClass);

    /**
     * Start doOutgoing instance with a doOutgoing definition and variables
     */
    ASFInstance startASFInstance(Class<? extends ASFDefinition> definitionClass, Map<String, Object> variables);

    /**
     * Load doOutgoing instance by id
     */
    ASFInstance findASFInstance(Class<? extends ASFDefinition> definitionClass, long id);
}
