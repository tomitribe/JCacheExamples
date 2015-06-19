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
package com.tomitribe.agumbrecht.producers;

import com.tomitribe.agumbrecht.interceptor.CacheInterceptor;
import com.tomitribe.agumbrecht.qualifiers.LocalCacheProvider;
import com.tomitribe.agumbrecht.qualifiers.ObjectCache;

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
