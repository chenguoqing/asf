package com.baidu.asf.engine;

/**
 * {@link ExecutionPath} represents a flow execution path,
 * it can back the path until to the StartEvent. That can be used to draw flow graph.
 */
public class ExecutionPath {
    /**
     * Source node full id
     */
    public final String sourceRef;
    /**
     * Target node full id
     */
    public final String targetRef;
    /**
     * Virtual flow?
     */
    public final boolean virtual;

    public ExecutionPath(String fromNodeId, String targetNodeId, boolean virtual) {
        this.sourceRef = fromNodeId;
        this.targetRef = targetNodeId;
        this.virtual = virtual;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(sourceRef).append("---->").append(targetRef).append(")");
        return sb.toString();
    }
}
