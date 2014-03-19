package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common implementation for Node,implementations should driven from this class
 */
public abstract class AbstractNode extends AbstractElement implements Node {
    protected final Node parent;
    protected final List<ExecutionListener> listeners = new ArrayList<ExecutionListener>();
    private Map<Flow, Node> successors = new HashMap<Flow, Node>();
    private Map<String, Node> predecessors = new HashMap<String, Node>();

    protected AbstractNode(String id, ActType actType, Node parent) {
        super(id, actType);
        this.parent = parent;
    }

    @Override
    public void addSuccessor(Flow flow, Node successor) {
        successors.put(flow, successor);
    }

    @Override
    public Map<Flow, Node> getSuccessors() {
        return new HashMap<Flow, Node>(successors);
    }

    @Override
    public void addPredecessor(String flowId, Node predecessor) {
        predecessors.put(flowId, predecessor);
    }

    @Override
    public Map<String, Node> getPredecessors() {
        return new HashMap<String, Node>(predecessors);
    }

    @Override
    public void addExecutionListener(ExecutionListener listener) {
        listeners.add(listener);
    }

    @Override
    public ExecutionListener[] getExecutionListeners() {
        return listeners.toArray(new ExecutionListener[0]);
    }

    @Override
    public Node getParent() {
        return null;
    }

    @Override
    public String getFullPath() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);

        Node parent = this.parent;

        while (parent != null) {
            builder.append("/").append(parent.getId());
        }
        return builder.reverse().toString();
    }
}
