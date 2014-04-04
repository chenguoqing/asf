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
     * From node full id
     */
    private String sourceRef;
    /**
     * Target node full id
     */
    private String targetRef;
    /**
     * Whether the flow is virtual?
     */
    private boolean isVirtualFlow;
    /**
     * From act type
     */
    private ActType fromActType;
    /**
     * Dest sourceRef's type
     */
    private ActType toActType;

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
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
