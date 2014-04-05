package com.baidu.asf.engine;

/**
 * {@link ASFEngineConfiguration} is responsible for building engine instance
 */
public interface ASFEngineConfiguration {

    /**
     * Build engine
     */
    ASFEngine buildEngine();

    /**
     * Decide the referenced object instance by ref name
     */
    <T> T getRefObject(String refName);

    /**
     * Decide the reference object instance by ref type, if multiple objects exist,
     * {@link com.baidu.asf.ASFException} will be thrown
     */
    <T> T getRefObject(Class<T> refType);
}
