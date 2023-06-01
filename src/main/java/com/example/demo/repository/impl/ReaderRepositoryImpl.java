package com.example.demo.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

import com.example.demo.exception.LibraryNotFoundException;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.repository.ReaderRepository;

@Repository
public class ReaderRepositoryImpl implements ReaderRepository {

    private static final Map<Long, Reader> DB_READER = new HashMap<>();

    @Override
    public Reader saveReader(Reader reader) {
        boolean ifExist = DB_READER.values().stream()
                .anyMatch(entry -> entry.getId().equals(reader.getId()));
        return ifExist ? DB_READER.put(reader.getId(), reader) :
                DB_READER.put(DB_READER.size() + 1L, reader);
    }

    @Override
    public Long deleteReader(Long id) {
        Reader reader = findReader(id)
                .orElseThrow(() -> new LibraryNotFoundException("Can't find such a reader"));
        DB_READER.remove(id);
        return reader.getId();
    }

    @Override
    public Optional<Reader> findReader(Long id) {
        return Optional.ofNullable(DB_READER.get(id));
    }

    @Override
    public List<Reader> findAll() {
        return new ArrayList<>(DB_READER.values());
    }

    @PostConstruct
    private void initReader() {
        Book book1 = new Book(1L,"Jane Air", "Charlotte Bronte",
                456, null);
        Book book2 = new Book(2L, "War and Peace", "Lev Tolstoy",
                968, null);
        Book book3 = new Book(3L, "Anna Karenina", "Lev Tolstoy",
                654, null);

        List<Book> victoriaBooks = new ArrayList<>();
        victoriaBooks.add(book1);
        victoriaBooks.add(book2);
        victoriaBooks.add(book3);

        Reader viktoria = new Reader(1L, "Victoria", "Kharchenko",
                victoriaBooks);
        DB_READER.put(1L, viktoria);

        List<Book> markBooks = new ArrayList<>();
        markBooks.add(book1);
        markBooks.add(book2);

        Reader mark = new Reader(2L, "Mark", "Trigulov", markBooks);
        DB_READER.put(2L, mark);

        List<Book> aliceBooks = new ArrayList<>();
        aliceBooks.add(book1);
        aliceBooks.add(book3);

        Reader alice = new Reader(3L, "Alice", "Smith", aliceBooks);
        DB_READER.put(3L, alice);
    }
}
