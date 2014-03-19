package com.baidu.asf.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ExecutionTracingNode} represents a flow execution path,
 * it can back the path until to the StartEvent. That can be used to draw flow graph.
 */
public class ExecutionTracingNode {
    public final String nodeId;
    private final Map<String, ExecutionTracingNode> successors = new HashMap<String, ExecutionTracingNode>();

    public ExecutionTracingNode(String nodeId) {
        this.nodeId = nodeId;
    }

    public void addSuccessor(String flow, ExecutionTracingNode node) {
        successors.put(flow, node);
    }

    public Map<String, ExecutionTracingNode> getSuccessors() {
        return successors;
    }
}
