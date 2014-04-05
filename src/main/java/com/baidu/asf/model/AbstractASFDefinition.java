package com.baidu.asf.model;

import java.util.*;

/**
 * The default implementation of definition
 */
public abstract class AbstractASFDefinition implements ASFDefinition {

    private String id;
    private String description;

    private ASFDefinition parent;
    private SubProcess parentNode;
    private int version;
    private StartEvent startEvent;
    private EndEvent endEvent;
    protected final Map<String, Node> nodes = new HashMap<String, Node>();
    protected final Set<Flow> flows = new HashSet<Flow>();

    protected final Map<String, ASFDefinition> subDefinitions = new HashMap<String, ASFDefinition>();

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public SubProcess getParentNode() {
        return parentNode;
    }

    public void setParentNode(SubProcess parentNode) {
        this.parentNode = parentNode;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public StartEvent getStartEvent() {
        return startEvent;
    }

    @Override
    public EndEvent getEndEvent() {
        return endEvent;
    }

    public void addNode(Node node) {
        if (node instanceof StartEvent) {
            if (startEvent != null && startEvent != node) {
                throw new ASFModelException("Multiple StartEvent has been found!");
            }
            this.startEvent = (StartEvent) node;
        } else if (node instanceof EndEvent) {
            if (endEvent != null && endEvent != node) {
                throw new ASFModelException("Multiple EndEvent has been found!");
            }
            this.endEvent = (EndEvent) node;
        }
        nodes.put(node.getId(), node);
        node.setParent(parentNode);

        if (node instanceof AbstractNode) {
            ((AbstractNode) node).setDefinition(this);
        }
    }

    @Override
    public <T extends Node> T getNode(String id) {
        return (T) nodes.get(id);
    }

    public void addFLow(Flow flow) {
        flows.add(flow);
    }

    @Override
    public <T extends Node> T findNode(String nodeFullId) {
        if (nodeFullId == null) {
            throw new NullPointerException("nodeFullId is null.");
        }

        String[] tokens = nodeFullId.split("/");
        String id = tokens[tokens.length - 1];
        ASFDefinition definition = this;

        if (id.isEmpty()) {
            throw new IllegalArgumentException("node id " + nodeFullId + " is invalidate");
        }

        for (int i = 0; i < tokens.length - 1; i++) {
            String path = tokens[i];
            if (path.isEmpty()) {
                throw new IllegalArgumentException("node id " + nodeFullId + " is invalidate");
            }

            definition = definition.getSubDefinition(path);
            if (definition == null) {
                throw new IllegalArgumentException("node id " + nodeFullId + " is invalidate");
            }
        }
        return (T) definition.getNode(id);
    }

    protected void setParent(ASFDefinition parent) {
        this.parent = parent;
    }

    @Override
    public ASFDefinition getParent() {
        return parent;
    }

    protected void addSubDefinition(String subProcessId, ASFDefinition subDefinition) {
        this.subDefinitions.put(subProcessId, subDefinition);
    }

    @Override
    public ASFDefinition getSubDefinition(String subDefinitionId) {
        return subDefinitions.get(subDefinitionId);
    }

    @Override
    public Map<String, ASFDefinition> getSubDefinitions() {
        return new HashMap<String, ASFDefinition>(subDefinitions);
    }

    protected void buildDefinition() {

        StartEvent startEvent = getStartEvent();
        EndEvent endEvent = getEndEvent();

        if (startEvent == null || endEvent == null) {
            throw new ASFModelException("The definition must have a StartEvent and a EndEvent.");
        }


        for (Flow flow : flows) {
            Node source = getNode(flow.getSourceRef());
            Node target = getNode(flow.getTargetRef());

            if (source == null) {
                throw new ASFModelException("Not found the source node :" + flow.getSourceRef());
            }

            if (target == null) {
                throw new ASFModelException("Not found the target node :" + flow.getTargetRef());
            }

            // no circle
            if (source == target) {
                throw new ASFModelException("Forbidden circle reference!(sourceRef=" + source.getId() + "," +
                        "targetRef=" + target.getId() + ")");
            }

            if (source == endEvent) {
                throw new ASFModelException("The flow sourceRef can not be EndEvent.");
            }

            if (target == startEvent) {
                throw new ASFModelException("The flow targetRef can not be StartEvent.");
            }
            source.addSuccessor(flow, target);
        }

        // validate model
        validateModel();
    }

    private void validateModel() {

        if (startEvent.getSuccessors().size() != 1) {
            throw new ASFModelException("The StartEvent must have only on outgoing flow.");
        }

        if (endEvent.getPredecessors().size() == 0) {
            throw new ASFModelException("The EndEvent must have at lest one incoming flow.");
        }

        final Map<String, Node> caches = new HashMap<String, Node>(nodes);

        // validate mode
        validateModel(startEvent, caches);

        // isolate nodes
        if (!caches.isEmpty()) {
            throw new ASFModelException("There are some isolated nodes." + Arrays.toString(caches.keySet().toArray(new
                    String[0])));
        }
    }

    private void validateModel(Node node, Map<String, Node> caches) {

        if (!caches.containsKey(node.getId())) {
            return;
        }

        // non-EndEvent node must has successors
        if (node.getSuccessors().isEmpty() && node != endEvent) {
            throw new ASFModelException("The node " + node.getId() + " have no any successors.");
        }

        // avoiding dead-loop
        caches.remove(node.getId());

        for (Node successor : node.getSuccessors().values()) {
            if (successor.getType() == NodeType.SubProcess) {
                SubProcess subProcess = (SubProcess) successor;
                ((AbstractASFDefinition) subProcess.getSubProcessDefinition()).buildDefinition();
            }

            validateModel(successor, caches);
        }
    }
}
