package com.example.demo.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Reader;
import com.example.demo.service.ReaderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readers")
public class ReaderController {
    private final ReaderService readerService;

    @GetMapping
    public ResponseEntity<List<Reader>> getAll() {
        return ResponseEntity.ok(readerService.findAllReaders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReader(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(readerService.findReader(id));
    }

    @PostMapping
    public ResponseEntity<Reader> saveReader(@RequestBody @Valid Reader reader) {
        return ResponseEntity.ok(readerService.saveReader(reader));
    }

    @PostMapping("/{readerId}/books/{bookId}")
    public ResponseEntity<Reader> takeBook(@PathVariable("readerId") @Valid Long readerId,
                                         @PathVariable("bookId") @Valid Long bookId) {
        return ResponseEntity.ok(readerService.takeBook(readerId, bookId));
    }

    @PostMapping("/return/{readerId}/books/{bookId}")
    public ResponseEntity<Reader> returnBook(@PathVariable("readerId") @Valid Long readerId,
                                             @PathVariable("bookId") @Valid Long bookId) {
        return ResponseEntity.ok(readerService.returnBook(readerId, bookId));
    }

    @DeleteMapping("/{id}")
    public Long deleteReader(@PathVariable @Valid Long id) {
        readerService.deleteReader(id);
        return id;
    }
}
