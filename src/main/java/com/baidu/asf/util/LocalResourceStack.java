package com.baidu.asf.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenguoqing01 on 14-4-3.
 */
public class LocalResourceStack {

    private static final ThreadLocal<CacheManager> threadCacheManager = new ThreadLocal<CacheManager>() {
        @Override
        protected CacheManager initialValue() {
            return new CacheManager();
        }
    };

    private static final ThreadLocal<AtomicInteger> threadRefCount = new ThreadLocal<AtomicInteger>() {
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };

    public static CacheManager getCacheManager() {
        return threadCacheManager.get();
    }

    public static void removeCacheManager() {
        threadCacheManager.remove();
    }

    public static AtomicInteger getRefCount() {
        return threadRefCount.get();
    }

    public static void removeRefCount() {
        threadRefCount.remove();
    }
}
