package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Reader;
import com.example.demo.repository.ReaderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepository;

    @Override
    public Reader saveReader(Reader reader) {
        return readerRepository.saveReader(reader);
    }

    @Override
    public Long deleteReader(Long id) {
        return readerRepository.deleteReader(id);
    }

    @Override
    public Optional<Reader> findReader(Long id) {
        return readerRepository.findReader(id);
    }
}
