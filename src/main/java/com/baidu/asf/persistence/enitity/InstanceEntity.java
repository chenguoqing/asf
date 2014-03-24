package com.baidu.asf.persistence.enitity;

/**
 * Process instance entity
 */
public class InstanceEntity extends Entity {
    /**
     * Process definition name
     */
    private String defId;
    /**
     * Process definition version
     */
    private int defVersion;
    /**
     * Process instance status
     */
    private int status;

    public String getDefId() {
        return defId;
    }

    public void setDefId(String defId) {
        this.defId = defId;
    }

    public int getDefVersion() {
        return defVersion;
    }

    public void setDefVersion(int defVersion) {
        this.defVersion = defVersion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
