package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Book;

public interface BookService {
    Book findBookById(Long id);

    List<Book> findAllBooks();

    Long deleteBook(Long id);

    Book saveBook(Book book);
}
