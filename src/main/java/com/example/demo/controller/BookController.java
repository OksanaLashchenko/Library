package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Book;
import com.example.demo.entity.dto.BookDto;
import com.example.demo.service.BookService;
import com.example.demo.service.dto.mapping.BookMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks()
        .stream()
        .map(bookMapper::bookToBookDto)
        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBookById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(bookMapper.bookToBookDto(bookService.findBookById(id)));
    }

    @PostMapping
    public ResponseEntity<BookDto> saveBook(@RequestBody @Valid Book book) {
        return ResponseEntity.ok(bookMapper.bookToBookDto(bookService.saveBook(book)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteBook(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }
}
