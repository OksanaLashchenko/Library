package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Reader;

public interface ReaderService {
    Reader saveReader(Reader reader);

    Long deleteReader(Long id);

    Optional<Reader> findReader(Long id);
}
