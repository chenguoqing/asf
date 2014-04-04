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
    private String fromActFullId;
    /**
     * To act id
     */
    private String toActFullId;
    /**
     * Whether the flow is virtual?
     */
    private boolean isVirtualFlow;
    /**
     * From act type
     */
    private ActType fromActType;
    /**
     * Dest fromNodeId's type
     */
    private ActType toActType;

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getFromActFullId() {
        return fromActFullId;
    }

    public void setFromActFullId(String fromActFullId) {
        this.fromActFullId = fromActFullId;
    }

    public String getToActFullId() {
        return toActFullId;
    }

    public void setToActFullId(String toActFullId) {
        this.toActFullId = toActFullId;
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
