package com.baidu.asf.model;

import com.baidu.asf.engine.VariableContext;
import com.baidu.asf.expression.ConditionExpression;

/**
 * Sequence flow
 */
public class SequenceFlow implements Flow {
    private String sourceRef;
    private String targetRef;
    private boolean isVirtual;
    private boolean isDefault;
    private ConditionExpression expression;

    public SequenceFlow() {
    }

    public SequenceFlow(String sourceRef, String targetRef) {
        this(sourceRef, targetRef, true, null);
    }

    public SequenceFlow(String sourceRef, String targetRef, ConditionExpression expression) {
        this(sourceRef, targetRef, false, expression);
    }

    public SequenceFlow(String sourceRef, String targetRef, boolean isVirtual, ConditionExpression expression) {
        this.sourceRef = sourceRef;
        this.targetRef = targetRef;
        this.isVirtual = isVirtual;
        this.expression = expression;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    @Override
    public String getSourceRef() {
        return sourceRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
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

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    public void setExpression(ConditionExpression expression) {
        this.expression = expression;
    }

    @Override
    public ConditionExpression getConditionExpression() {
        return expression;
    }

    @Override
    public boolean evaluate(VariableContext scope) {
        return expression == null ? true : expression.evaluate(scope);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(sourceRef).append("---->").append(targetRef).append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SequenceFlow)) {
            return false;
        }

        SequenceFlow target = (SequenceFlow) obj;

        return sourceRef.equals(target.sourceRef) && targetRef.equals(target.targetRef);
    }

    @Override
    public int hashCode() {
        return sourceRef.hashCode() + 31 * targetRef.hashCode();
    }
}
