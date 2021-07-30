package com.example.demo.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Book;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final Map<Long, Book> DB_BOOK = new HashMap<>();

    @Override
    public Book saveBook(Book book) {
        boolean ifExist = DB_BOOK.values().stream()
                .anyMatch(e -> e.getBookId().equals(book.getBookId()));
        return ifExist ? DB_BOOK.put(book.getBookId(), book) :
                DB_BOOK.put(DB_BOOK.size() + 1L, book);
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(DB_BOOK.values());
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        return Optional.ofNullable(DB_BOOK.get(id));
    }

    @Override
    public Long deleteBook(Long id) {
        try {
            DB_BOOK.remove(id);
        } catch (LibraryNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found", ex);
        }
        return id;
    }

    @PostConstruct
    private void initBook() {
        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("Jane Air");
        book1.setAuthor("Charlotte Bronte");
        book1.setPages(456);

        Book book2 = new Book();
        book2.setBookId(2L);
        book2.setTitle("War and Peace");
        book2.setAuthor("Lev Tolstoy");
        book2.setPages(968);

        Book book3 = new Book();
        book3.setBookId(3L);
        book3.setTitle("Anna Karenina");
        book3.setAuthor("Lev Tolstoy");
        book3.setPages(654);

        DB_BOOK.put(1L, book1);
        DB_BOOK.put(2L, book2);
        DB_BOOK.put(3L, book3);
    }
}
