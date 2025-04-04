package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.domain.dto.BookDto;
import com.example.demo.mapper.BookMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Book;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto findBookById(Long id) {
        Book book = bookRepository.findBookById(id)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("Can't find book by id %d", id)));
        return bookMapper.toBookDto(book);
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
