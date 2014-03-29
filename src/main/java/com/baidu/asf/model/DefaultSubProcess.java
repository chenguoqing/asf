package com.baidu.asf.model;

/**
 * SubProcess
 */
public class DefaultSubProcess extends AbstractNode implements SubProcess {

    private ASFDefinition subDefinition;

    public DefaultSubProcess() {
        setActType(ActType.SubProcess);
    }

    public void setSubProcessDefinition(ASFDefinition subDefinition) {
        this.subDefinition = subDefinition;
    }

    @Override
    public ASFDefinition getSubProcessDefinition() {
        return subDefinition;
    }
}
