package com.baidu.asf.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of definition
 */
public abstract class AbstractASFDefinition extends AbstractElement implements ASFDefinition {

    private ASFDefinition parent;
    private int version;
    private StartEvent startEvent;
    private EndEvent endEvent;
    protected final Map<String, Node> nodes = new HashMap<String, Node>();
    protected final Map<String, Flow> flows = new HashMap<String, Flow>();

    protected final Map<String, ASFDefinition> subDefinitions = new HashMap<String, ASFDefinition>();

    @Override
    public ActType getType() {
        return ActType.Definition;
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
    }

    @Override
    public <T extends Node> T getNode(String id) {
        return (T) nodes.get(id);
    }

    public void addFLow(Flow flow) {
        flows.put(flow.getId(), flow);
    }

    public Map<String, Flow> getFlows() {
        return new HashMap<String, Flow>(flows);
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
        return definition == null ? null : (T) definition.getNode(id);
    }

    protected void setParent(ASFDefinition parent) {
        this.parent = parent;
    }

    @Override
    public ASFDefinition getParent() {
        return parent;
    }

    public void addSubDefinition(ASFDefinition subDefinition) {
        this.subDefinitions.put(subDefinition.getId(), subDefinition);
    }

    @Override
    public ASFDefinition getSubDefinition(String subDefinitionId) {
        return subDefinitions.get(subDefinitionId);
    }

    @Override
    public Map<String, ASFDefinition> getSubDefinitions() {
        return new HashMap<String, ASFDefinition>(subDefinitions);
    }

    protected void buildDefinition(AbstractASFDefinition definition) {

        StartEvent startEvent = definition.getStartEvent();
        EndEvent endEvent = definition.getEndEvent();

        if (startEvent == null || endEvent == null) {
            throw new ASFModelException("The definition must have a StartEvent and a EndEvent.");
        }


        for (String flowId : definition.flows.keySet()) {
            Flow flow = definition.flows.get(flowId);
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
        validateModel(definition);
    }

    private void validateModel(AbstractASFDefinition definition) {

        if (startEvent.getSuccessors().size() != 1) {
            throw new ASFModelException("The StartEvent must have only on outgoing flow.");
        }

        if (endEvent.getPredecessors().size() == 0) {
            throw new ASFModelException("The EndEvent must have at lest ony incoming flow.");
        }

        final Map<String, Node> caches = new HashMap<String, Node>(nodes);

        // validate mode
        validateModel(definition, startEvent, caches);

        // isolate nodes
        if (!caches.isEmpty()) {
            throw new ASFModelException("There are some isolate nodes.");
        }
    }

    private void validateModel(AbstractASFDefinition definition, Node node, Map<String, Node> caches) {

        if (!caches.containsKey(node.getId())) {
            return;
        }

        // non-EndEvent node must has successors
        if (node.getSuccessors().isEmpty() && node != definition.endEvent) {
            throw new ASFModelException("The node " + node.getId() + " have no any successors.");
        }

        // avoiding dead-loop
        caches.remove(node.getId());

        for (Node successor : definition.startEvent.getSuccessors().values()) {
            if (successor.getType() == ActType.SubProcess) {
                SubProcess subProcess = (SubProcess) successor;
                buildDefinition((AbstractASFDefinition) subProcess.getSubProcessDefinition());
            }

            validateModel(definition, successor, caches);
        }
    }
}
