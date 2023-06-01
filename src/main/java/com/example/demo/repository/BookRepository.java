package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Book;

public interface BookRepository {

    Book saveBook(Book book);

    List<Book> findAll();

    Optional<Book> findBookById(Long id);

    Long deleteBook(Long id);
}
