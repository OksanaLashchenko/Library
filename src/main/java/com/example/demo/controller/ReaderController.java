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

import com.example.demo.entity.Reader;
import com.example.demo.entity.dto.ReaderDto;
import com.example.demo.service.ReaderService;
import com.example.demo.service.dto.mapping.ReaderMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readers")
public class ReaderController {
    private final ReaderService readerService;
    private final ReaderMapper readerMapper;

    @GetMapping
    public ResponseEntity<List<ReaderDto>> getAll() {
        return ResponseEntity.ok(readerService.findAllReaders()
                .stream()
                .map(readerMapper::readerToReaderDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDto> getReader(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(readerMapper.readerToReaderDto(readerService.findReader(id)));
    }

    @PostMapping
    public ResponseEntity<ReaderDto> saveReader(@RequestBody @Valid Reader reader) {
        return ResponseEntity.ok(readerMapper.readerToReaderDto(
                readerService.saveReader(reader)));
    }

    @PostMapping("/{readerId}/books/{bookId}")
    public ResponseEntity<ReaderDto> takeBook(@PathVariable("readerId") @Valid Long readerId,
                                         @PathVariable("bookId") @Valid Long bookId) {
        return ResponseEntity.ok(readerMapper.readerToReaderDto(
                readerService.takeBook(readerId, bookId)));
    }

    @PostMapping("/return/{readerId}/books/{bookId}")
    public ResponseEntity<ReaderDto> returnBook(@PathVariable("readerId") @Valid Long readerId,
                                             @PathVariable("bookId") @Valid Long bookId) {
        return ResponseEntity.ok(readerMapper.readerToReaderDto(
                readerService.returnBook(readerId, bookId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteReader(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(readerService.deleteReader(id));
    }
}
