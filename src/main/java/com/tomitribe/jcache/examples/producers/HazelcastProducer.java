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
package com.tomitribe.jcache.examples.producers;

import com.hazelcast.cache.impl.HazelcastServerCachingProvider;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.tomitribe.jcache.examples.qualifiers.Hazelcast;
import com.tomitribe.jcache.examples.qualifiers.LocalCacheProvider;

import javax.cache.CacheManager;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.net.URL;

@ApplicationScoped
public class HazelcastProducer {

    @Produces
    @Singleton
    @Hazelcast
    public HazelcastInstance createHazelcastInstance() {

        final String configFile = "META-INF/hazelcast.xml";
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URL location = loader.getResource(configFile);
        final Config config = new Config();

        config.setConfigurationUrl(location);
        config.setInstanceName("TomEEInstance");

        return com.hazelcast.core.Hazelcast.newHazelcastInstance(config);
    }

    @Produces
    @Singleton
    @LocalCacheProvider
    public CacheManager createCacheManager(@Hazelcast final HazelcastInstance instance) {
        return HazelcastServerCachingProvider.createCachingProvider(instance).getCacheManager();
    }

    public void close(@Disposes @Hazelcast final HazelcastInstance instance) {
        instance.shutdown();
    }
}
