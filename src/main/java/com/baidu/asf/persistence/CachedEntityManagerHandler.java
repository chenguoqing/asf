package com.baidu.asf.persistence;

import com.baidu.asf.cache.Cache;
import com.baidu.asf.cache.CacheManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Cache proxy
 */
public class CachedEntityManagerHandler implements InvocationHandler {
    private final EntityManager target;

    public CachedEntityManagerHandler(EntityManager target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Cache cache = method.getAnnotation(Cache.class);
        CacheKey key = new CacheKey(method, args);
        Object v;
        if (cache != null && method.getReturnType() != Void.class) {
            v = CacheManager.getCache(key);

            if (v != null) {
                return v;
            }
        }

        v = method.invoke(target, args);
        CacheManager.addCache(key, v);

        return v;
    }

    public EntityManager getProxy() {
        return (EntityManager) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{EntityManager.class}, this);
    }

    static class CacheKey {
        final Method method;
        final Object[] args;

        CacheKey(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }

        @Override
        public int hashCode() {
            Object[] newArgs = new Object[args.length + 1];
            newArgs[0] = method;
            System.arraycopy(args, 0, newArgs, 0, args.length);
            return Arrays.hashCode(newArgs);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !(obj instanceof CacheKey)) {
                return false;
            }

            CacheKey target = (CacheKey) obj;
            return target.method == method && Arrays.equals(args, target.args);
        }

    }
}
