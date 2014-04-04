package com.baidu.asf.engine;

/**
 * {@link ExecutionPath} represents a flow execution path,
 * it can back the path until to the StartEvent. That can be used to draw flow graph.
 */
public class ExecutionPath {
    public final String fromNodeId;
    public final String targetNodeId;
    public final boolean virtual;

    public ExecutionPath(String fromNodeId, String targetNodeId, boolean virtual) {
        this.fromNodeId = fromNodeId;
        this.targetNodeId = targetNodeId;
        this.virtual = virtual;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(fromNodeId).append("---->").append(targetNodeId).append(")");
        return sb.toString();
    }
}
