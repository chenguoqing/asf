package com.baidu.asf.engine;

import com.baidu.asf.engine.command.CommandExecutor;
import com.baidu.asf.engine.command.CommandInterceptor;

/**
 * {@link ASFEngineConfiguration} is responsible for building engine instance
 */
public interface ASFEngineConfiguration {

    /**
     * Build engine
     */
    ASFEngine buildEngine();

    CommandInterceptor buildCommandInterceptor();
}
