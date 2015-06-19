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
package com.tomitribe.jcache.examples.producers;

import com.tomitribe.jcache.examples.qualifiers.LocalCacheProvider;
import com.tomitribe.jcache.examples.qualifiers.ObjectCache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ObjectCacheProducer {

    private static final String CACHE_NAME = System.getProperty("jcache.cache.name", "object_cache");
    public static final int DURATION = Integer.parseInt(System.getProperty("jcache.cache.duration", "60"));

    @Produces
    @Singleton
    @ObjectCache
    public Cache<String, Object> createUserCache(@LocalCacheProvider final CacheManager cacheManager) {

        final MutableConfiguration<String, Object> config = new MutableConfiguration<String, Object>();
        config.setStoreByValue(true)
                .setTypes(String.class, Object.class)
                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, DURATION)))
                .setStatisticsEnabled(false);

        return cacheManager.createCache(CACHE_NAME, config);
    }
}
