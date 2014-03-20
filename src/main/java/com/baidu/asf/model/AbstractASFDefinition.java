package com.baidu.asf.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of definition
 */
public abstract class AbstractASFDefinition extends AbstractElement implements ASFDefinition {

    protected ASFDefinition parent;
    protected int version;
    protected StartEvent startEvent;
    protected EndEvent endEvent;
    protected final Map<String, Node> nodes = new HashMap<String, Node>();

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

    @Override
    public <T extends Node> T getNode(String id) {
        return (T) nodes.get(id);
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


    @Override
    public ASFDefinition getParent() {
        return parent;
    }

    @Override
    public ASFDefinition getSubDefinition(String subDefinitionId) {
        return subDefinitions.get(subDefinitionId);
    }

    @Override
    public Map<String, ASFDefinition> getSubDefinitions() {
        return new HashMap<String, ASFDefinition>(subDefinitions);
    }
}
