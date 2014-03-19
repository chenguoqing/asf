package com.baidu.asf.persistence.enitity;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Variable entity
 */
public class VariableEntity2 extends Entity {
    /**
     * Process instance id
     */
    private long instanceId;
    /**
     * Process instance id
     */
    private long topInstanceId;
    /**
     * Variables
     */
    private final Map<String, Object> variables = new HashMap<String, Object>();

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public long getTopInstanceId() {
        return topInstanceId;
    }

    public void setTopInstanceId(long topInstanceId) {
        this.topInstanceId = topInstanceId;
    }

    public <T> T getVariable(String name) {
        return (T) variables.get(name);
    }

    /**
     * Return the variable newContext
     */
    public Map<String, Object> getVariables() {
        return new HashMap<String, Object>(variables);
    }

    public void setVariable(String name, Object value) {
        this.variables.put(name, value);
    }

    public void setVariables(Map<String, Object> variables) {
        if (variables != null) {
            this.variables.putAll(variables);
        }
    }

    /**
     * Serialize all variables to json
     */
    public byte[] serializeVariables() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsBytes(variables);
        } catch (IOException e) {
            //ignore
        }
        return null;
    }
}
