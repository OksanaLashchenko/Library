package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Reader;

public interface ReaderService {
    Reader saveReader(Reader reader);

    Long deleteReader(Long id);

    Reader findReader(Long id);

    List<Reader> findAllReaders();

    Reader takeBook(Long readerId, Long bookId);

    Reader returnBook(Long readerId, Long bookId);
}
