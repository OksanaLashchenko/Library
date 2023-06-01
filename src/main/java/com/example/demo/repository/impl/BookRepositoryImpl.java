package com.example.demo.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

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
        Book book = findBookById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Book not found"));
        DB_BOOK.remove(id);
        return book.getBookId();
    }

    @PostConstruct
    private void initBook() {
        Book book1 = new Book(1L,"Jane Air", "Charlotte Bronte",
                456, null);
        Book book2 = new Book(2L, "War and Peace", "Lev Tolstoy",
                968, null);
        Book book3 = new Book(3L, "Anna Karenina", "Lev Tolstoy",
                654, null);

        DB_BOOK.put(1L, book1);
        DB_BOOK.put(2L, book2);
        DB_BOOK.put(3L, book3);
    }

}
