/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tomitribe.agumbrecht;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.tomitribe.agumbrecht.application.BookService;
import com.tomitribe.agumbrecht.entities.Book;
import com.tomitribe.agumbrecht.interceptor.CacheInterceptor;
import com.tomitribe.agumbrecht.producers.HazelcastProducer;
import com.tomitribe.agumbrecht.producers.ObjectCacheProducer;
import com.tomitribe.agumbrecht.qualifiers.Hazelcast;
import com.tomitribe.agumbrecht.qualifiers.LocalCacheProvider;
import com.tomitribe.agumbrecht.qualifiers.ObjectCache;
import com.tomitribe.agumbrecht.service.InterceptedService;
import com.tomitribe.agumbrecht.service.ServiceApplication;
import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Arquillian will start the container, deploy all @Deployment bundles, then run all the @Test methods.
 * <p/>
 * A strong value-add for Arquillian is that the test is abstracted from the server.
 * It is possible to rerun the same test against multiple adapters or server configurations.
 * <p/>
 * A second value-add is it is possible to build WebArchives that are slim and trim and therefore
 * isolate the functionality being tested.  This also makes it easier to swap out one implementation
 * of a class for another allowing for easy mocking.
 */
@RunWith(Arquillian.class)
public class InterceptedServiceTest extends Assert {

    /**
     * ShrinkWrap is used to create a war file on the fly.
     * <p/>
     * The API is quite expressive and can build any possible
     * flavor of war file.  It can quite easily return a rebuilt
     * war file as well.
     * <p/>
     * More than one @Deployment method is allowed.
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        ServiceApplication.class,
                        BookService.class,
                        Book.class,
                        CacheInterceptor.class,
                        ObjectCache.class,
                        LocalCacheProvider.class,
                        Hazelcast.class,
                        ObjectCacheProducer.class,
                        HazelcastProducer.class,
                        InterceptedService.class)
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsResource("openejb-jar.xml", "META-INF/openejb-jar.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    /**
     * This URL will contain the following URL data
     * <p/>
     * - http://<host>:<port>/<webapp>/
     * <p/>
     * This allows the test itself to be agnostic of server information or even
     * the name of the webapp
     */
    @ArquillianResource
    private URL url;

    @Inject
    private BookService bookService;

    @Test
    public void getBook() throws Exception {

        Book book = new Book();
        book.setBookTitle("War and Peace");

        final int id = bookService.addBook(book);

        //First call caches stored
        book = getClient().path("api/intercept/" + id).get(Book.class);

        assertEquals("Invalid book id", id, book.getBookId());
        assertEquals("Invalid book name", "War and Peace", book.getBookTitle());

        bookService.update(id, "Harry met Sally");

        //Get cached version
        book = getClient().path("api/intercept/" + id).get(Book.class);

        assertEquals("Invalid book id", id, book.getBookId());
        assertEquals("Invalid book name", "War and Peace", book.getBookTitle());

        //Get stored version
        bookService.clear();
        book = getClient().path("api/intercept/" + id).get(Book.class);

        assertEquals("Invalid book id", id, book.getBookId());
        assertEquals("Invalid book name", "Harry met Sally", book.getBookTitle());

    }

    private WebClient getClient() throws URISyntaxException {
        final List<JacksonJaxbJsonProvider> pl = Arrays.asList(new JacksonJaxbJsonProvider());
        return WebClient.create(url.toURI().toASCIIString(), pl).type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);
    }
}
