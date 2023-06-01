package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Book;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.impl.BookServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findBookById_WhenBookNotExist_ThenThrowLibraryNotFoundException() {
        when(bookRepository.findBookById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> bookService.findBookById(1L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void findBookById_WhenBookExists_ThenReturnBook() {
        Book book = new Book(1L, "Effective Java", "Bloch",
                786, null);
        when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));

        Book resultBook = bookService.findBookById(1L);

        Assertions.assertThat(resultBook.getBookId()).isEqualTo(1L);
        Assertions.assertThat(resultBook.getTitle()).isEqualTo("Effective Java");
        Assertions.assertThat(resultBook.getAuthor()).isEqualTo("Bloch");
        Assertions.assertThat(resultBook.getPages()).isEqualTo(786);
        Assertions.assertThat(resultBook.getReader()).isNull();
    }

    @Test
    void findAllBooks_WhenBooksExist_ThenReturnListOfAllBooks() {
        Book book1 = new Book(1L,"Jane Air", "Charlotte Bronte",
                456, null);
        Book book2 = new Book(2L, "War and Peace", "Lev Tolstoy",
                968, null);
        Book book3 = new Book(3L, "Anna Karenina", "Lev Tolstoy",
                654, null);

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);

        when(bookRepository.findAll()).thenReturn(bookList);
        Assertions.assertThat(bookService.findAllBooks()).isEqualTo(bookList);
    }

    @Test
    void findAllBooks_WhenBooksNotExist_ThenReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertThat(bookService.findAllBooks()).isEqualTo(Collections.emptyList());
    }

    @Test
    void deleteBook_WhenBookExists_ThenReturnBookId() {
        when(bookRepository.deleteBook(1L)).thenReturn(1L);
        Assertions.assertThat(bookService.deleteBook(1L)).isEqualTo(1L);
    }

    @Test
    void deleteBook_WhenBookNotExist_ThenThrowLibraryNotFoundException() {
        when(bookRepository.deleteBook(4L))
                .thenThrow(LibraryNotFoundException.class);

        Assertions.assertThatThrownBy(() -> bookService.deleteBook(4L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void saveBook_WhenBookExists_ThenUpdateBook() {
        Book book = new Book(1L, "Effective Java", "Bloch",
                786, null);
        when(bookRepository.saveBook(book)).thenReturn(book);

        Book resultBook = bookService.saveBook(book);

        Assertions.assertThat(resultBook.getBookId()).isEqualTo(1L);
        Assertions.assertThat(resultBook.getTitle()).isEqualTo("Effective Java");
        Assertions.assertThat(resultBook.getAuthor()).isEqualTo("Bloch");
        Assertions.assertThat(resultBook.getPages()).isEqualTo(786);
        Assertions.assertThat(resultBook.getReader()).isNull();
    }

    @Test
    void saveBook_WhenBookNotExists_ThenSaveNewBook() {
        Book book = new Book(4L, "Lord of the Rings", "Tolkien",
                1186, null);
        when(bookRepository.saveBook(book)).thenReturn(book);

        Book resultBook = bookService.saveBook(book);

        Assertions.assertThat(resultBook.getBookId()).isEqualTo(4L);
        Assertions.assertThat(resultBook.getTitle()).isEqualTo("Lord of the Rings");
        Assertions.assertThat(resultBook.getAuthor()).isEqualTo("Tolkien");
        Assertions.assertThat(resultBook.getPages()).isEqualTo(1186);
        Assertions.assertThat(resultBook.getReader()).isNull();
    }
}
