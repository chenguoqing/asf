package com.baidu.asf.model;

import com.baidu.asf.engine.VariableContext;
import com.baidu.asf.expression.ConditionExpression;

/**
 * Sequence flow
 */
public class SequenceFlow extends AbstractElement implements Flow {
    private String sourceRef;
    private String targetRef;
    private boolean isVirtual;
    private ConditionExpression expression;

    public SequenceFlow() {
        setActType(ActType.Flow);
    }

    public void setSourceRef(String nodeId) {
        this.sourceRef = nodeId;
    }

    @Override
    public String getSourceRef() {
        return sourceRef;
    }

    public void setTargetRef(String nodeId) {
        this.targetRef = nodeId;
    }

    @Override
    public String getTargetRef() {
        return targetRef;
    }

    public void setVirtual(boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    @Override
    public boolean isVirtual() {
        return isVirtual;
    }

    @Override
    public void setConditionExpression(ConditionExpression expression) {
        this.expression = expression;
    }

    @Override
    public boolean evaluate(VariableContext scope) {
        return expression == null ? true : expression.evaluate(scope);
    }
}
