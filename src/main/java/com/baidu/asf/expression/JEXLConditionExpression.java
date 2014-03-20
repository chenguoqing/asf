package com.baidu.asf.expression;

import com.baidu.asf.engine.VariableContext;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

/**
 * The implementation of JEXL
 */
public class JEXLConditionExpression implements ConditionExpression {

    private final JexlEngine engine = new JexlEngine();
    private String expression;
    private Expression jexlExpression;

    @Override
    public void setExpression(String expression) {
        if (expression != null) {
            this.expression = expression;
        }
    }

    @Override
    public void validate() {
        if (expression != null) {
            this.jexlExpression = engine.createExpression(expression);
        }
    }

    @Override
    public boolean evaluate(VariableContext scope) {
        if (expression == null) {
            return true;
        }

        if (jexlExpression == null) {
            this.jexlExpression = engine.createExpression(expression);
        }
        Object result = jexlExpression.evaluate(new VariableJexlContext(scope));

        return (result instanceof Boolean) ? (Boolean) result : false;
    }

    static class VariableJexlContext implements JexlContext {
        final VariableContext variableContext;

        VariableJexlContext(VariableContext variableContext) {
            this.variableContext = variableContext;
        }

        @Override
        public Object get(String name) {
            return variableContext.getVariable(name);
        }

        @Override
        public void set(String name, Object value) {
            variableContext.setVariable(name, value);
        }

        @Override
        public boolean has(String name) {
            return variableContext.getVariable(name) != null;
        }
    }
}
