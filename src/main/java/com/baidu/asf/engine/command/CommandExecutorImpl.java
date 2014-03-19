package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;

/**
 * The default implementation of {@link CommandExecutor}
 */
public class CommandExecutorImpl implements CommandExecutor {
    private CommandInterceptor first;

    @Override
    public void setCommandInterceptor(CommandInterceptor first) {
        this.first = first;
    }

    @Override
    public <T> T execute(ProcessorContext context, Command<T> command) {
        return first.execute(context, command);
    }
}
