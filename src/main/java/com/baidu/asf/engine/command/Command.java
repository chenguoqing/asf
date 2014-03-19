package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;

/**
 * ASF uses the command pattern,all operation should be wrapped with {@link Command} interface and execution on the
 * chain
 */
public interface Command<T> {
    /**
     * Execute the command
     */
    T execute(ProcessorContext context);
}
