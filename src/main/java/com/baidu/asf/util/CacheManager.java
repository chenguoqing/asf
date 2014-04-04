package com.baidu.asf.util;

import java.util.HashMap;

/**
 * Simple cache interface
 */
public class CacheManager extends HashMap<Object, Object> {

    public void addCache(Object key, Object value) {
        put(key, value);
    }

    public Object getCache(Object key) {
        return get(key);
    }

    public void clearCaches() {
        clear();
    }
}
