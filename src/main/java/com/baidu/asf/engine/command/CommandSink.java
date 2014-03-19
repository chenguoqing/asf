package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;

/**
 * The sink interceptor should invoke the special command
 */
public class CommandSink extends AbstractCommandInterceptor {
    @Override
    public <T> T execute(ProcessorContext context, Command<T> command) {
        return command.execute(context);
    }
}
