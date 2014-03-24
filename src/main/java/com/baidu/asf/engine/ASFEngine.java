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
    ASFInstance startASFInstance(ASFDefinition definition);

    /**
     * Start doOutgoing instance with a doOutgoing definition and variables
     */
    ASFInstance startASFInstance(ASFDefinition definition, Map<String, Object> variables);

    /**
     * Load doOutgoing instance by id
     */
    ASFInstance findASFInstance(ASFDefinition definition, long id);
}
