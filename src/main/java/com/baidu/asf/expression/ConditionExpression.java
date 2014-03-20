package com.baidu.asf.expression;

import com.baidu.asf.engine.VariableContext;

/**
 * Condition expression that will be bound to flow,the syntax refers to <a href="http://commons.apache
 * .org/proper/commons-jexl/">JEXL</a>
 */
public interface ConditionExpression {

    /**
     * Set the literal expression,the expression validation will be delayed to {@link #validate()}
     */
    void setExpression(String expression);

    /**
     * Validate the expression syntax
     *
     * @throws com.baidu.asf.expression.ExpressionException for any syntax errors
     */
    void validate();

    /**
     * Evaluate the expression, if the result is not <b>true</b>, <b>false</b> as the default value
     */
    boolean evaluate(VariableContext scope);
}
