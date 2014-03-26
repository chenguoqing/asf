package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common implementation for Node,implementations should driven from this class
 */
public abstract class AbstractNode implements Node {
    private String id;
    private ActType actType;
    private String name;
    private String description;
    protected Node parent;
    protected final List<ExecutionListener> listeners = new ArrayList<ExecutionListener>();
    private Map<Flow, Node> successors = new HashMap<Flow, Node>();
    private Map<Flow, Node> predecessors = new HashMap<Flow, Node>();

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setActType(ActType actType) {
        this.actType = actType;
    }

    @Override
    public ActType getType() {
        return actType;
    }

    @Override
    public void addSuccessor(Flow flow, Node successor) {
        successors.put(flow, successor);
        successor.addPredecessor(flow, this);
    }

    @Override
    public Map<Flow, Node> getSuccessors() {
        return new HashMap<Flow, Node>(successors);
    }

    @Override
    public void addPredecessor(Flow flow, Node predecessor) {
        predecessors.put(flow, predecessor);
    }

    @Override
    public Map<Flow, Node> getPredecessors() {
        return new HashMap<Flow, Node>(predecessors);
    }

    @Override
    public void addExecutionListener(ExecutionListener listener) {
        listeners.add(listener);
    }

    @Override
    public ExecutionListener[] getExecutionListeners() {
        return listeners.toArray(new ExecutionListener[0]);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public String getFullId() {
        StringBuilder builder = new StringBuilder();

        Node node = this;

        while (node != null) {
            builder.insert(0, node.getId());
            node = node.getParent();
            if (node != null) {
                builder.insert(0, "/");
            }
        }
        return builder.toString();
    }
}
