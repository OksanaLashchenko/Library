package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.exception.LibraryAlreadyBookedException;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;
import com.example.demo.service.impl.ReaderServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ReaderServiceImpl readerService;

    @Test
    void findReader_WhenReaderNotExist_ThenThrowLibraryNotFoundException() {
        Mockito.when(readerRepository.findReader(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> readerService.findReader(1L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void findReader_WhenReaderExists_ThenReturnReader() {
        Reader reader = new Reader(4L, "Soyer", "Tom", null);
        Mockito.when(readerRepository.findReader(4L)).thenReturn(Optional.of(reader));
        Reader resultReader = readerService.findReader(4L);
        Assertions.assertThat(resultReader.getId()).isEqualTo(4L);
        Assertions.assertThat(resultReader.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(resultReader.getLastName()).isEqualTo("Soyer");
        Assertions.assertThat(resultReader.getBooks()).isNull();
    }

    @Test
    void deleteReader_WhenReaderExists_ThenReturnReaderId() {
        Reader reader = new Reader(1L, "Soyer", "Tom", Collections.emptyList());
        Mockito.when(readerRepository.findReader(1L)).thenReturn(Optional.of(reader));
        Mockito.when(readerRepository.deleteReader(1L)).thenReturn(1L);
        Assertions.assertThat(readerService.deleteReader(1L)).isEqualTo(1L);
    }

    @Test
    void deleteReader_WhenReaderNotExists_ThenReturnLibraryNotFoundException() {
        Mockito.when(readerRepository.findReader(4L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.deleteReader(4L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void saveReader_WhenReaderExists_ThenUpdateReader() {
        Reader reader = new Reader(1L, "Soyer", "Tom", null);
        Mockito.when(readerRepository.saveReader(reader)).thenReturn(reader);
        Reader resultReader = readerService.saveReader(reader);
        Assertions.assertThat(resultReader.getId()).isEqualTo(1L);
        Assertions.assertThat(resultReader.getLastName()).isEqualTo("Soyer");
        Assertions.assertThat(resultReader.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(resultReader.getBooks()).isNull();
    }

    @Test
    void saveReader_WhenReaderNotExists_ThenSaveReader() {
        Reader reader = new Reader(4L, "Soyer", "Tom", null);
        Mockito.when(readerRepository.saveReader(reader)).thenReturn(reader);
        Reader resultReader = readerService.saveReader(reader);
        Assertions.assertThat(resultReader.getId()).isEqualTo(4L);
        Assertions.assertThat(resultReader.getLastName()).isEqualTo("Soyer");
        Assertions.assertThat(resultReader.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(resultReader.getBooks()).isNull();
    }

    @Test
    void takeBook_WhenBookIsAvailable_ThenReturnReader() {
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, null);
        Reader reader = new Reader(4L, "Soyer", "Tom", new ArrayList<>());
        Mockito.when(readerRepository.findReader(reader.getId()))
                        .thenReturn(Optional.of(reader));
        Mockito.when(bookRepository.findBookById(book.getBookId()))
                .thenReturn(Optional.of(book));
        Assertions.assertThat(readerService.takeBook(reader.getId(), book.getBookId()))
                .isEqualTo(reader);
    }

    @Test
    void takeBook_WhenBookNotExists_ThenThrowLibraryNotFoundException() {
        Mockito.when(bookRepository.findBookById(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.takeBook(4L, 5L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void takeBook_WhenReaderNotExists_ThenThrowLibraryNotFoundException() {
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, null);
        Mockito.when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));
        Mockito.when(readerRepository.findReader(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.takeBook(5L, 1L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void takeBook_WhenBookIsTaken_ThenThrowLibraryAlreadyBookedException() {
        Reader readerTom = new Reader(4L, "Soyer", "Tom", new ArrayList<>());
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, readerTom);
        Reader readerAlice = new Reader(6L, "WonderLand", "Alice",
                new ArrayList<>());
        Mockito.when(readerRepository.findReader(6L)).thenReturn(Optional.of(readerAlice));
        Mockito.when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));
        Assertions.assertThatThrownBy(() -> readerService.takeBook(6L,
                1L)).isInstanceOf(LibraryAlreadyBookedException.class);
    }

    @Test
    void returnBook_WhenIsOk_ThenReturnReader() {
        Reader reader = new Reader(4L, "Soyer", "Tom", new ArrayList<>());
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, reader);
        Mockito.when(readerRepository.findReader(reader.getId()))
                .thenReturn(Optional.of(reader));
        Mockito.when(bookRepository.findBookById(book.getBookId()))
                .thenReturn(Optional.of(book));
        Assertions.assertThat(readerService.returnBook(reader.getId(), book.getBookId()))
                .isEqualTo(reader);
    }

    @Test
    void returnBook_WhenBookNotExists_ThenThrowLibraryNotFoundException() {
        Mockito.when(bookRepository.findBookById(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.returnBook(4L, 5L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void returnBook_WhenReaderNotExists_ThenThrowLibraryNotFoundException() {
        Reader readerTom = new Reader(4L, "Soyer", "Tom", new ArrayList<>());
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, readerTom);
        Mockito.when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));
        Mockito.when(readerRepository.findReader(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.returnBook(5L,
                1L)).isInstanceOf(LibraryNotFoundException.class);
    }
}
