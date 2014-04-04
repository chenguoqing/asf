package com.baidu.asf.engine;

/**
 * {@link ASFEngineConfiguration} is responsible for building engine instance
 */
public interface ASFEngineConfiguration {

    /**
     * Build engine
     */
    ASFEngine buildEngine();
}
