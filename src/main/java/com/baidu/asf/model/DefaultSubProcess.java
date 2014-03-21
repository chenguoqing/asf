package com.baidu.asf.model;

/**
 * SubProcess
 */
public class DefaultSubProcess extends AbstractNode implements SubProcess {

    private ASFDefinition subDefinition;

    public DefaultSubProcess(Node parent) {
        setActType(ActType.SubProcess);
        setParent(parent);
    }

    public void setSubProcessDefinition(ASFDefinition subDefinition) {
        this.subDefinition = subDefinition;
    }

    @Override
    public ASFDefinition getSubProcessDefinition() {
        return subDefinition;
    }
}
