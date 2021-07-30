package com.example.demo.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

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
        DB_READER.remove(id);
        return id;
    }

    @Override
    public Optional<Reader> findReader(Long id) {
        return Optional.ofNullable(DB_READER.get(id));
    }

    @PostConstruct
    private void initReader() {
        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("Jane Air");
        book1.setAuthor("Charlotte Bronte");
        book1.setPages(456);

        Book book2 = new Book();
        book2.setBookId(2L);
        book2.setTitle("War and Peace");
        book2.setAuthor("Lev Tolstoy");
        book2.setPages(968);

        Book book3 = new Book();
        book3.setBookId(3L);
        book3.setTitle("Anna Karenina");
        book3.setAuthor("Lev Tolstoy");
        book3.setPages(654);

        Reader viktoria = new Reader();
        viktoria.setId(1L);
        viktoria.setFirstName("Victoria");
        viktoria.setLastName("Kharchenko");
        List<Book> victoriaBooks = new ArrayList<>();
        victoriaBooks.add(book1);
        victoriaBooks.add(book2);
        victoriaBooks.add(book3);
        viktoria.setBooks(victoriaBooks);
        DB_READER.put(1L, viktoria);

        Reader mark = new Reader();
        mark.setId(2L);
        mark.setFirstName("Mark");
        mark.setLastName("Trigulov");
        List<Book> markBooks = new ArrayList<>();
        markBooks.add(book1);
        markBooks.add(book2);
        mark.setBooks(markBooks);
        DB_READER.put(2L, mark);

        Reader alice = new Reader();
        alice.setId(3L);
        alice.setFirstName("Alice");
        alice.setLastName("Smith");
        List<Book> aliceBooks = new ArrayList<>();
        aliceBooks.add(book1);
        aliceBooks.add(book3);
        alice.setBooks(aliceBooks);
        DB_READER.put(3L, alice);

        Reader bob = new Reader();
        bob.setId(4L);
        bob.setFirstName("Bob");
        bob.setLastName("Busset");
        List<Book> bobBooks = new ArrayList<>();
        bobBooks.add(book2);
        bobBooks.add(book3);
        bob.setBooks(bobBooks);
    }
}
