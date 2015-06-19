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

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.net.URL;

@ApplicationScoped
public class CacheProducer {

    @Produces
    @Singleton
    @LocalCacheProvider
    public CacheManager createCacheManager() {

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

        throw new UnsupportedOperationException("Unknown provider");
    }

    public void close(@Disposes @LocalCacheProvider final CacheManager instance) {
        instance.close();
    }
}
