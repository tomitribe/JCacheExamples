/**
 * Tomitribe Confidential
 * <p/>
 * Copyright(c) Tomitribe Corporation. 2014
 * <p/>
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 * <p/>
 */
package com.tomitribe.jcache.examples.interceptor;

import com.tomitribe.jcache.examples.qualifiers.ObjectCache;

import javax.cache.Cache;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CacheInvalidator {

    @Inject
    @ObjectCache
    private Cache<String, Object> cache;

    @AroundInvoke
    public Object cache(final InvocationContext ctx) throws Exception {

        final String name = ctx.getTarget().getClass().getName();
        final Method method = ctx.getMethod();
        final Object[] parameters = ctx.getParameters();

        final String s = Arrays.toString(parameters);

        final String key = name + ":" + method.getName() + ":" + s;

        Object o = cache.get(key);

        if (null == o) {
            o = ctx.proceed();
            cache.put(key, o);
        }

        return o;
    }
}
