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
     * Node fromNodeId id
     */
    private String nodeFullId;
    /**
     * Current fromNodeId type
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

    public String getNodeFullId() {
        return nodeFullId;
    }

    public void setNodeFullId(String nodeFullId) {
        this.nodeFullId = nodeFullId;
    }

    public ActType getActType() {
        return actType;
    }

    public void setActType(ActType actType) {
        this.actType = actType;
    }
}
