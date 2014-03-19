package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;

/**
 * {@link CommandExecutor} is the main entry for all commands,it invokes the execution chain and prepare the
 * command context
 */
public interface CommandExecutor {

    void setCommandInterceptor(CommandInterceptor first);

    <T> T execute(ProcessorContext context, Command<T> command);
}
