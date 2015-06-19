/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.tomitribe.jcache.examples.interceptor;

import com.tomitribe.jcache.examples.qualifiers.ObjectCache;

import javax.cache.Cache;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class CacheInterceptor {

    @Inject
    @ObjectCache
    private Cache<String, Object> cache;

    @AroundInvoke
    public Object cache(final InvocationContext ctx) throws Exception {

        final Object[] parameters = ctx.getParameters();

        final String key = parameters[0].toString();

        Object o = cache.get(key);

        if (null == o) {
            o = ctx.proceed();
            cache.put(key, o);
        }

        return o;
    }
}
