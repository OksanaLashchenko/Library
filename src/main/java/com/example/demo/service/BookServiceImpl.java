package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.exception.LibraryAppException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Override
    public Book findBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findBookById(id);
        if (optionalBook.isEmpty()) {
            throw new LibraryAppException("Can't find book by id " + id);
        }
        return optionalBook.get();
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Long deleteBook(Long id) {
        return bookRepository.deleteBook(id);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.saveBook(book);
    }

    @Override
    public void addBookToReader(Long bookId, Long readerId) {
        Book bookToAdd = findBookById(bookId);
        Reader reader = readerRepository.findReader(readerId).orElse(new Reader(readerId));
        List<Book> books = reader.getBooks();
        if (books.stream()
                .noneMatch(book -> book.equals(bookToAdd))) {
            books.add(bookToAdd);
        } else {
            throw new LibraryAppException("There is such a book by id " + bookId
                    + " by reader " + reader);
        }
    }

    @Override
    public Book takeBook(Long bookId, Long readerId) {
        Book book = findBookById(bookId);
        Reader reader = readerRepository.findReader(readerId).orElse(new Reader(readerId));
        if (book.getReader() == null) {
            book.setReader(reader);
            addBookToReader(bookId, readerId);
        } else {
            throw new LibraryAppException("Book is unavailable for taking");
        }
        return book;
    }
}
