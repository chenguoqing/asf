package com.baidu.asf.engine;

/**
 * System variables operation interface,system variable only be used within ASF engine.
 */
public interface SystemVariableContext {
    /**
     * Set variable
     */
    void setSystemVariable(String name, Object value);

    /**
     * Retrieve variable by name
     */
    Object getSystemVariable(String name);

    /**
     * Remove variable by name
     */
    void removeSystemVariable(String name);

    /**
     * Remove all system variables
     */
    void clearSystemVariables();

    /**
     * Atomically increments by one the current value.
     */
    int incrementAndGet(String name);
}
