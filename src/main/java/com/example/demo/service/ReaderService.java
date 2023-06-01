package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;

import java.util.List;

public interface ReaderService {
    Reader saveReader(Reader reader);

    Long deleteReader(Long id);

    Reader findReader(Long id);

    Reader takeBook(Long readerId, Long bookId);

    Reader returnBook(Long readerId, Long bookId);

    List<Reader> findAllReaders();

}
