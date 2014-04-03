package com.baidu.asf.model;

import com.baidu.asf.engine.VariableContext;
import com.baidu.asf.expression.ConditionExpression;

/**
 * {@link com.baidu.asf.model.Flow} is a connection be nodes between nodes,it may be a real connection(that can be
 * defined within process definition),else be a virtual connection, that only be representing a connective semantic
 * (for tracing execution history, we can create a virtual connection from SubProcess nodeId to StartEvent of sub
 * process.Actually, the connection is not exists with user perspective.)
 */
public interface Flow {
    /**
     * Whether the flow is virtual?
     */
    boolean isVirtual();

    /**
     * The flow is default?
     */
    boolean isDefault();

    /**
     * From node id
     */
    String getSourceRef();

    String getTargetRef();

    /**
     * Bound a expression
     */
    ConditionExpression getConditionExpression();

    boolean evaluate(VariableContext context);
}
