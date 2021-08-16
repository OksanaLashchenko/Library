package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.exception.LibraryAlreadyBookedException;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;
import com.example.demo.service.ReaderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Reader saveReader(Reader reader) {
        return readerRepository.save(reader);
    }

    @Override
    @Transactional
    public Long deleteReader(Long id) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Can't find such a reader"));
        reader.getBooks().stream()
                .map(Book::getReaders)
                .forEach(setReaders -> setReaders.remove(reader));
        readerRepository.delete(reader);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Reader findReader(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("Can't find reader by id %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reader> findAllReaders() {
        return readerRepository.findAll();
    }

    @Override
    @Transactional
    public Reader takeBook(Long readerId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a book with id %d", bookId)));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a reader with id %d", readerId)));
        boolean nonExists = book.getReaders().stream()
                .noneMatch(readerInSet -> readerInSet.equals(reader));
        if (nonExists) {
            reader.addBook(book);
            book.getReaders().add(reader);
        } else {
            throw new LibraryAlreadyBookedException("Book is unavailable for taking");
        }
        return reader;
    }

    @Override
    @Transactional
    public Reader returnBook(Long readerId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a book with id %d", bookId)));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a reader with id %d", readerId)));
        book.getReaders().stream()
                .filter(readerInSet -> readerInSet.equals(reader))
                .forEach(readerToEmpty -> readerToEmpty = null);
        reader.removeBook(book);
        return reader;
    }
}
