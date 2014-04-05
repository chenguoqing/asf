package com.baidu.asf.persistence.enitity;

import com.baidu.asf.model.NodeType;

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
    private NodeType sourceNodeType;
    /**
     * Dest sourceRef's type
     */
    private NodeType targetNodeType;

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

    public NodeType getSourceNodeType() {
        return sourceNodeType;
    }

    public void setSourceNodeType(NodeType sourceNodeType) {
        this.sourceNodeType = sourceNodeType;
    }

    public NodeType getTargetNodeType() {
        return targetNodeType;
    }

    public void setTargetNodeType(NodeType targetNodeType) {
        this.targetNodeType = targetNodeType;
    }
}
