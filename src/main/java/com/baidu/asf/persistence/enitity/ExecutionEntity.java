package com.baidu.asf.persistence.enitity;

import com.baidu.asf.model.ActType;

/**
 * Process execution entity
 */
public class ExecutionEntity extends Entity {
    /**
     * Associated doOutgoing instance
     */
    private long instanceId;
    /**
     * Node nodeId id
     */
    private String actFullPath;
    /**
     * Current nodeId type
     */
    private ActType actType;
    /**
     * Parent act id path("/" split)
     */

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getActFullPath() {
        return actFullPath;
    }

    public void setActFullPath(String actFullPath) {
        this.actFullPath = actFullPath;
    }

    public ActType getActType() {
        return actType;
    }

    public void setActType(ActType actType) {
        this.actType = actType;
    }
}
