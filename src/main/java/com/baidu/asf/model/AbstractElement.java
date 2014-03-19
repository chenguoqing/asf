package com.baidu.asf.model;

/**
 * Abstract implementation
 */
public abstract class AbstractElement implements ActElement {

    public final String id;
    public final ActType actType;
    private String name;
    private String description;

    protected AbstractElement(String id, ActType actType) {
        this.id = id;
        this.actType = actType;
    }

    @Override
    public void setId(String id) {
        throw new IllegalArgumentException("Forbidden!");
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

    @Override
    public ActType getType() {
        return actType;
    }
}
