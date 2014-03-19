package com.baidu.asf.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ExecutionPathNode} represents a flow execution path,
 * it can back the path until to the StartEvent. That can be used to draw flow graph.
 */
public class ExecutionPathNode {
    public final String nodeId;
    private final Map<String, ExecutionPathNode> successors = new HashMap<String, ExecutionPathNode>();

    public ExecutionPathNode(String nodeId) {
        this.nodeId = nodeId;
    }

    public void addSuccessor(String flow, ExecutionPathNode node) {
        successors.put(flow, node);
    }

    public Map<String, ExecutionPathNode> getSuccessors() {
        return successors;
    }
}
