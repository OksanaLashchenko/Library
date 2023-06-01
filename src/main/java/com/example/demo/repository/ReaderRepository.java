package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Book;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Reader;

public interface ReaderRepository {

    Reader saveReader(Reader reader);

    Long deleteReader(Long id);

    Optional<Reader> findReader(Long id);

    List<Reader> findAll();

}
