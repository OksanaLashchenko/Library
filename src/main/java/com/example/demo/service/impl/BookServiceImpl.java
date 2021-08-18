package com.example.demo.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Book;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("Can't find book by id %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Long deleteBook(Long id) {
        bookRepository.delete(findBookById(id));
        return id;
    }

    @Override
    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * initBook() is a method designed for pre-initializing some books before starting the
     * Library application.
     */
    @PostConstruct
    public void initBook() {
        Book book1 = new Book(1L, "Jane Air", "Charlotte Bronte",
                456, Collections.emptySet());
        Book book2 = new Book(2L, "War and Peace", "Lev Tolstoy",
                968, Collections.emptySet());
        Book book3 = new Book(3L, "Anna Karenina", "Lev Tolstoy",
                654, Collections.emptySet());

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }
}
