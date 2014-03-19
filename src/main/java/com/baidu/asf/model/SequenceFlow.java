package com.baidu.asf.model;

import com.baidu.asf.engine.VariableContext;

/**
 * Sequence flow
 */
public class SequenceFlow extends AbstractElement implements Flow {
    private final boolean isVirtual;
    private ConditionExpression expression;

    public SequenceFlow(String id) {
        this(id, false);
    }

    public SequenceFlow(String id, boolean isVirtual) {
        super(id, ActType.StartEvent);
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
