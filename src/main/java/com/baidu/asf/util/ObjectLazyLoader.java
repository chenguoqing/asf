package com.baidu.asf.util;

/**
 * {@link ObjectLazyLoader} will delay the construction of concrete object instance
 */
public interface ObjectLazyLoader<T> {
    /**
     * Construct the object instance
     *
     * @throws {@link com.baidu.asf.ASFException} if the object fails to construct
     */
    T getObject();
}
