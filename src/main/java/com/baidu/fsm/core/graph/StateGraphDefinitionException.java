package com.baidu.fsm.core.graph;

import com.baidu.fsm.core.StateMachineException;

/**
 * The exception wrapped some error information for state graph definition
 */
public class StateGraphDefinitionException extends StateMachineException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param format the detail message. The detail message is saved for
     *               later retrieval by the {@link #getMessage()} method.
     */
    public StateGraphDefinitionException(String format, Object... args) {
        super(String.format(format, args));
    }
}
