package com.baidu.asf.engine;

import java.util.Map;

/**
 * Variable operation interface for user space
 */
public interface VariableContext {
    /**
     * Set variable
     */
    void setVariable(String name, Object value);

    /**
     * Set variables
     */
    void setVariables(Map<String, Object> variables);

    /**
     * Retrieve variable by name
     */
    Object getVariable(String name);

    /**
     * Remove variable by name
     */
    void removeVariable(String name);

    /**
     * Clear all variables
     */
    void clearVariables();
}
