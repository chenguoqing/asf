package com.baidu.asf.persistence.enitity;

/**
 * Process instance entity
 */
public class InstanceEntity extends Entity {
    /**
     * Process definition name
     */
    private String defName;
    /**
     * Process definition version
     */
    private int defVersion;
    /**
     * Process definition class name
     */
    private String defClassName;
    /**
     * Process instance status
     */
    private int status;

    public String getDefName() {
        return defName;
    }

    public void setDefName(String defName) {
        this.defName = defName;
    }

    public int getDefVersion() {
        return defVersion;
    }

    public void setDefVersion(int defVersion) {
        this.defVersion = defVersion;
    }

    public String getDefClassName() {
        return defClassName;
    }

    public void setDefClassName(String defClassName) {
        this.defClassName = defClassName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
