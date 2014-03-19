package com.baidu.asf.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenguoqing01 on 14-3-6.
 */
public class CacheManager {

    private static final ThreadLocal<Map<Object, Object>> cacheLocal = new ThreadLocal<Map<Object, Object>>() {
        @Override
        protected Map<Object, Object> initialValue() {
            return new HashMap<Object, Object>();
        }
    };

    public static void addCache(Object key, Object value) {
        cacheLocal.get().put(key, value);
    }

    public static Object getCache(Object key) {
        return cacheLocal.get().get(key);
    }

    public static void clearCaches() {
        cacheLocal.remove();
    }
}
