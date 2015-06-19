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
package com.tomitribe.agumbrecht.service;

import com.tomitribe.agumbrecht.application.BookService;
import com.tomitribe.agumbrecht.entities.Book;
import com.tomitribe.agumbrecht.interceptor.CacheInterceptor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("intercept")
public class InterceptedService {

    @Inject
    private BookService bookService;

    @Path("{id}")
    @GET
    @Produces({APPLICATION_JSON})
    @Interceptors({CacheInterceptor.class})
    public Book getBook(@PathParam("id") final int id) {

        final List<Book> allBooks = bookService.getAllBooks();
        for (final Book book : allBooks) {
            if (id == book.getBookId()) {
                return book;
            }
        }

        throw new WebApplicationException(404);
    }
}
