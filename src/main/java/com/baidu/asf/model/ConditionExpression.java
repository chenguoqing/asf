package com.baidu.asf.model;

import com.baidu.asf.engine.VariableContext;

/**
 * Created by chenguoqing01 on 14-3-3.
 */
public interface ConditionExpression {

    void setExpression(String expression);

    boolean evaluate(VariableContext scope);
}
