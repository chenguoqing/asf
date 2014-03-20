package com.baidu.asf.model;

/**
 * Abstract implementation
 */
public abstract class AbstractElement implements ActElement {

    private String id;
    private ActType actType;
    private String name;
    private String description;

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
}
