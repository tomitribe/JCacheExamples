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

import com.tomitribe.jcache.examples.qualifiers.CacheImplementation;
import com.tomitribe.jcache.examples.qualifiers.LocalCacheProvider;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.net.URL;

@ApplicationScoped
public class CacheProducer {

//    @Produces
//    @Singleton
//    @CacheImplementation
//    public Object createCacheInstance() {
//
//        //Quick hack, but simple
//        final String provider = System.getProperty("tomee.cache.provider", "hazelcast");
//
//        if ("hazelcast".equalsIgnoreCase(provider)) {
//            final String configFile = "META-INF/hazelcast.xml";
//            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
//            final URL location = loader.getResource(configFile);
//            final Config config = new Config();
//
//            config.setConfigurationUrl(location);
//            config.setInstanceName("TomEEInstance");
//
//            return com.hazelcast.core.Hazelcast.newHazelcastInstance(config);
//        } else if ("ehcache".equalsIgnoreCase(provider)) {
//
//            final URL url = getClass().getResource("/anotherconfigurationname.xml");
//            // CacheManager manager = CacheManager.newInstance(url);
//
//        } //TODO - More....
//
//        throw new UnsupportedOperationException("Unknown provider");
//    }

    @Produces
    @Singleton
    @LocalCacheProvider
    public CacheManager createCacheManager() {

        Object instance = null;

        //Quick hack, but simple
        final String provider = System.getProperty("tomee.cache.provider", "hazelcast");

        if ("hazelcast".equalsIgnoreCase(provider)) {
            return Caching
                    .getCachingProvider("com.hazelcast.cache.impl.HazelcastServerCachingProvider")
                    .getCacheManager();
        } else if ("ehcache".equalsIgnoreCase(provider)) {

            final URL url = getClass().getResource("/anotherconfigurationname.xml");
            // CacheManager manager = CacheManager.newInstance(url);

        } //TODO - More....

//        if (HazelcastInstance.class.isInstance(instance)) {
//            return HazelcastServerCachingProvider.createCachingProvider(HazelcastInstance.class.cast(instance)).getCacheManager();
//        }

        throw new UnsupportedOperationException("Unknown provider");
    }

//    public void close(@Disposes @CacheImplementation final HazelcastInstance instance) {
//        instance.shutdown();
//    }
}
