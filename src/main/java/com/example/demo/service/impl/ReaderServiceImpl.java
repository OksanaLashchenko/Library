package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.exception.LibraryAlreadyBookedException;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;
import com.example.demo.service.ReaderService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Override
    public Reader saveReader(Reader reader) {
        return readerRepository.saveReader(reader);
    }

    @Override
    public Long deleteReader(Long id) {
        Reader reader = readerRepository.findReader(id)
                .orElseThrow(() -> new LibraryNotFoundException("Can't find such a reader"));
        reader.getBooks().stream()
                .map(Book::getReader)
                .forEach(readerToDelete -> readerToDelete = null);
        return readerRepository.deleteReader(id);
    }

    @Override
    public Reader findReader(Long id) {
        return readerRepository.findReader(id)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("Can't find reader by id %d", id)));
    }

    @Override
    public Reader takeBook(Long readerId, Long bookId) {
        Book book = bookRepository.findBookById(bookId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a book with id %d", bookId)));
        Reader reader = readerRepository.findReader(readerId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a reader with id %d", readerId)));
        if (book.getReader() == null) {
            book.setReader(reader);
            List<Book> books = reader.getBooks();
            books.add(book);
        } else {
            throw new LibraryAlreadyBookedException("Book is unavailable for taking");
        }
        return reader;
    }

    @Override
    public Reader returnBook(Long readerId, Long bookId) {
        Book book = bookRepository.findBookById(bookId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a book with id %d", bookId)));
        Reader reader = readerRepository.findReader(readerId)
                .orElseThrow(() -> new LibraryNotFoundException(
                        String.format("There is no such a reader with id %d", readerId)));
        book.setReader(null);
        List<Book> books = reader.getBooks();
        books.remove(book);
        return reader;
    }

    @Override
    public List<Reader> findAllReaders() {
        return readerRepository.findAll();
    }

}
