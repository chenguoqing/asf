package com.baidu.asf.persistence.enitity;

import com.baidu.asf.model.ActType;

/**
 * Transition entity
 */
public class TransitionEntity extends Entity {
    /**
     * Associated doOutgoing instance
     */
    private long instanceId;
    /**
     * From act id
     */
    private String fromActFullPath;
    /**
     * To act id
     */
    private String toActFullPath;
    /**
     * Flow id
     */
    private String flowId;
    /**
     * Whether the flow is virtual?
     */
    private boolean isVirtualFlow;
    /**
     * From act type
     */
    private ActType fromActType;
    /**
     * Dest nodeId's type
     */
    private ActType toActType;

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getFromActFullPath() {
        return fromActFullPath;
    }

    public void setFromActFullPath(String fromActFullPath) {
        this.fromActFullPath = fromActFullPath;
    }

    public String getToActFullPath() {
        return toActFullPath;
    }

    public void setToActFullPath(String toActFullPath) {
        this.toActFullPath = toActFullPath;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public boolean isVirtualFlow() {
        return isVirtualFlow;
    }

    public void setVirtualFlow(boolean isVirtualFlow) {
        this.isVirtualFlow = isVirtualFlow;
    }

    public ActType getFromActType() {
        return fromActType;
    }

    public void setFromActType(ActType fromActType) {
        this.fromActType = fromActType;
    }

    public ActType getToActType() {
        return toActType;
    }

    public void setToActType(ActType toActType) {
        this.toActType = toActType;
    }
}
