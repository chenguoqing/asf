package com.baidu.asf.engine;

import com.baidu.asf.model.Flow;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ExecutionPathNode} represents a flow execution path,
 * it can back the path until to the StartEvent. That can be used to draw flow graph.
 */
public class ExecutionPathNode {
    public final String nodeId;
    private final Map<Flow, ExecutionPathNode> successors = new HashMap<Flow, ExecutionPathNode>();

    public ExecutionPathNode(String nodeId) {
        this.nodeId = nodeId;
    }

    public void addSuccessor(Flow flow, ExecutionPathNode node) {
        successors.put(flow, node);
    }

    public Map<Flow, ExecutionPathNode> getSuccessors() {
        return successors;
    }
}
