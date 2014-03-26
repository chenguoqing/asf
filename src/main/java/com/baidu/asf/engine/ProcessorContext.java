package com.baidu.asf.engine;

import com.baidu.asf.model.ASFDefinition;
import com.baidu.asf.persistence.EntityManager;

import java.util.Map;

/**
 * {@link ProcessorContext} encapsulates the execution context for {@link com.baidu.asf.engine.processor
 * .ExecutionProcessor} and {@link com.baidu.asf.engine.command.Command}
 */
public interface ProcessorContext {
    /**
     * Controlled parameter for interceptor execution
     */
    public static enum ParamKeys {
        /**
         * Disable cache for command
         */
        DisabledCache("cache.disabled"),
        TransactionPropagation("propagation.level");

        public final String paramName;

        ParamKeys(String paramName) {
            this.paramName = paramName;
        }
    }

    /**
     * Associated doOutgoing instance
     */
    ASFInstance getInstance();

    /**
     * Associated process definition, if the process is top process, the definition is same as {@link
     * ASFInstance#getDefinition};otherwise,it is a sub process special definition
     */
    ASFDefinition getDefinition();

    /**
     * Return associated EntityManager for context
     */
    void setEntityManager(EntityManager entityManager);

    /**
     * Associated entity manager
     */
    EntityManager getEntityManager();

    /**
     * Current execution user task id bound with persist;return 0 for other nodes.
     */
    long getExecutionTaskId();

    /**
     * Add control parameters for under command interceptors
     */
    void addParam(String name, Object value);

    void addParams(Map<String, Object> params);

    <T> T getParam(String name);

    Map<String, Object> getParams();
}
