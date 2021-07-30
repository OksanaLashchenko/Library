package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.service.ReaderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readers")
public class ReaderController {
    private final ReaderService readerService;

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReader(@PathVariable Long id) {
        return ResponseEntity.ok(readerService.findReader(id));
    }

    @PostMapping
    public ResponseEntity<Reader> saveReader(@RequestBody Reader reader) {
        return ResponseEntity.ok(readerService.saveReader(reader));
    }

    @PostMapping("/take")
    public ResponseEntity<Book> takeBook(@RequestParam("readerId") Long readerId,
                                         @RequestParam("bookId") Long bookId) {
        return ResponseEntity.ok(readerService.takeBook(readerId, bookId));
    }

    @DeleteMapping("/{id}")
    public Long deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return id;
    }
}
