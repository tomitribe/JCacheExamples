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
package com.tomitribe.agumbrecht.application;

import com.tomitribe.agumbrecht.entities.Book;
import com.tomitribe.agumbrecht.qualifiers.ObjectCache;

import javax.cache.Cache;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Stateless
public class BookService {


    @Inject
    @ObjectCache
    private Cache<String, Object> cache;

    @PersistenceContext(unitName = "book-pu")
    private EntityManager entityManager;

    public int addBook(final Book book) {
        entityManager.persist(book);
        return book.getBookId();
    }

    public List<Book> getAllBooks() {
        final CriteriaQuery<Book> cq = entityManager.getCriteriaBuilder().createQuery(Book.class);
        cq.select(cq.from(Book.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public void update(final int id, final String title) {
        final Book book = entityManager.find(Book.class, id);
        book.setBookTitle(title);
    }

    public void clear(){
        cache.clear();
    }
}
