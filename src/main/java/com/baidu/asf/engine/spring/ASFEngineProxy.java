package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ASFInstance;

import java.util.Map;

/**
 * {@link ASFEngineProxy} is a simple interface for engine operation on spring context
 */
public interface ASFEngineProxy {
    /**
     * Start doOutgoing instance with a doOutgoing definition
     */
    ASFInstance startASFInstance();

    /**
     * Start doOutgoing instance with a doOutgoing definition and variables
     */
    ASFInstance startASFInstance(Map<String, Object> variables);

    /**
     * Load doOutgoing instance by id
     */
    ASFInstance findASFInstance(long id);
}
