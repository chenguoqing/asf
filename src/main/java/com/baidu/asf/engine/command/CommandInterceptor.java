package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;

/**
 * {@link CommandInterceptor} represents the pattern of interceptor chain,it connects all interceptors together with
 * one chain, and command execution will be filtered by each interceptor on chain.
 */
public interface CommandInterceptor {

    /**
     * Execute the command with interceptor
     */
    <T> T execute(ProcessorContext context, Command<T> command);

    /**
     * Set the nodeId interceptor
     */
    void setNext(CommandInterceptor next);
}
