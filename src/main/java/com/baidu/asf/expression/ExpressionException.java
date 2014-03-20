package com.baidu.asf.expression;

import com.baidu.asf.ASFException;

/**
 * {@link ExpressionException} represents a syntax error for expression
 */
public class ExpressionException extends ASFException {
    /**
     * The evaluated expression
     */
    public final String expression;

    public ExpressionException(String expression, Throwable e) {
        super(e);
        this.expression = expression;
    }

    public ExpressionException(String expression, String message, Throwable e) {
        super(message, e);
        this.expression = expression;
    }
}
