package com.example.demo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.stereotype.Service;

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
    public Book findBookById(Long id) {
        return bookRepository.findBookById(id)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("Can't find book by id %d", id)));
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Long deleteBook(Long id) {
        return bookRepository.deleteBook(id);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.saveBook(book);
    }

}
