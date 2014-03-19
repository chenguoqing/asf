package com.baidu.fsm.storage;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * The state machine entry for persist
 */
public class StateMachineEntity {

    /**
     * Unique id
     */
    private long id;
    /**
     * Parent id
     */
    private long parentId;
    /**
     * Associated sub machine ids
     */
    private final Map<String, Long> subMachines = new HashMap<String, Long>();

    /**
     * The updated version for optimistic lock update
     */
    private int version;
    /**
     * Status(0-active;1-inactive)
     */
    private int status;
    /**
     * Transition table
     */
    private byte[] transitions;
    /**
     * Current state
     */
    private String state;
    /**
     * The state definition version for tracking the changes history
     */
    private int stateDefVersion;
    /**
     * All associated variables
     */
    private final Map<String, Serializable> variables = new HashMap<String, Serializable>();
    private Timestamp created;
    private Timestamp modified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte[] getTransitions() {
        return transitions;
    }

    public void setTransitions(byte[] transitions) {
        this.transitions = transitions;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStateDefVersion() {
        return stateDefVersion;
    }

    public void setStateDefVersion(int stateDefVersion) {
        this.stateDefVersion = stateDefVersion;
    }

    public void addSubMachine(String name, long id) {
        subMachines.put(name, id);
    }

    public long getSubMachine(String name) {
        return subMachines.get(name);
    }

    public Map<String, Long> getSubMachines() {
        return new HashMap<String, Long>(subMachines);
    }

    public void setVariable(String name, Serializable value) {
        variables.put(name, value);
    }

    public <T extends Serializable> T getVariable(String name) {
        return (T) variables.get(name);
    }

    public Map<String, Serializable> getVariables() {
        return new HashMap<String, Serializable>(variables);
    }

    public <T extends Serializable> T removeVariable(String name) {
        return (T) variables.remove(name);
    }

    public void clearVariables() {
        variables.clear();
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
