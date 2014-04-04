package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.util.LocalResourceStack;

/**
 * {@link ASFEntranceInterceptor} is the first interceptor of chain
 */
public class ASFEntranceInterceptor extends AbstractCommandInterceptor {
    @Override
    public <T> T execute(ProcessorContext context, Command<T> command) {
        LocalResourceStack.getRefCount().incrementAndGet();

        try {
            return next.execute(context, command);
        } finally {
            int count = LocalResourceStack.getRefCount().decrementAndGet();
            if (count == 0) {
                LocalResourceStack.removeCacheManager();
            }
        }
    }
}
