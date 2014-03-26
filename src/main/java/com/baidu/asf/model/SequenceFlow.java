package com.baidu.asf.model;

import com.baidu.asf.engine.VariableContext;
import com.baidu.asf.expression.ConditionExpression;

/**
 * Sequence flow
 */
public class SequenceFlow implements Flow {
    private final String sourceRef;
    private final String targetRef;
    private final boolean isVirtual;
    private final ConditionExpression expression;

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

    @Override
    public String getSourceRef() {
        return sourceRef;
    }

    @Override
    public String getTargetRef() {
        return targetRef;
    }

    @Override
    public boolean isVirtual() {
        return isVirtual;
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
