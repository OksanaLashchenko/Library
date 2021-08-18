package com.example.demo.service;

import static org.mockito.Mockito.times;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        Mockito.when(readerRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> readerService.findReader(1L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void findReader_WhenReaderExists_ThenReturnReader() {
        Reader reader = new Reader(4L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", Collections.emptySet());
        Mockito.when(readerRepository.findById(4L)).thenReturn(Optional.of(reader));
        Reader resultReader = readerService.findReader(4L);
        Assertions.assertThat(resultReader.getId()).isEqualTo(4L);
        Assertions.assertThat(resultReader.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(resultReader.getLastName()).isEqualTo("Soyer");
        Assertions.assertThat(resultReader.getBooks()).isEmpty();
    }

    @Test
    void deleteReader_WhenReaderExists_ThenReturnReaderId() {
        Reader reader = new Reader(1L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", Collections.emptySet());
        Mockito.when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        readerService.deleteReader(1L);
        Mockito.verify(readerRepository, times(1)).delete(reader);
    }

    @Test
    void deleteReader_WhenReaderNotExists_ThenReturnLibraryNotFoundException() {
        Mockito.when(readerRepository.findById(4L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.deleteReader(4L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void saveReader_WhenReaderExists_ThenUpdateReader() {
        Reader reader = new Reader(1L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", Collections.emptySet());
        Mockito.when(readerRepository.save(reader)).thenReturn(reader);
        Reader resultReader = readerService.saveReader(reader);
        Assertions.assertThat(resultReader.getId()).isEqualTo(1L);
        Assertions.assertThat(resultReader.getLastName()).isEqualTo("Soyer");
        Assertions.assertThat(resultReader.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(resultReader.getBooks()).isEmpty();
    }

    @Test
    void saveReader_WhenReaderNotExists_ThenSaveReader() {
        Reader reader = new Reader(4L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", Collections.emptySet());
        Mockito.when(readerRepository.save(reader)).thenReturn(reader);
        Reader resultReader = readerService.saveReader(reader);
        Assertions.assertThat(resultReader.getId()).isEqualTo(4L);
        Assertions.assertThat(resultReader.getLastName()).isEqualTo("Soyer");
        Assertions.assertThat(resultReader.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(resultReader.getBooks()).isEmpty();
    }

    @Test
    void takeBook_WhenBookIsAvailable_ThenReturnReader() {
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, new HashSet<>());
        Reader reader = new Reader(4L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", new HashSet<>());
        Mockito.when(readerRepository.findById(reader.getId()))
                        .thenReturn(Optional.of(reader));
        Mockito.when(bookRepository.findById(book.getBookId()))
                .thenReturn(Optional.of(book));
        Assertions.assertThat(readerService.takeBook(4L, 1L))
                .isEqualTo(reader);
    }

    @Test
    void takeBook_WhenBookNotExists_ThenThrowLibraryNotFoundException() {
        Mockito.when(bookRepository.findById(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.takeBook(4L, 5L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void takeBook_WhenReaderNotExists_ThenThrowLibraryNotFoundException() {
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, Collections.emptySet());
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(readerRepository.findById(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.takeBook(5L, 1L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void takeBook_WhenBookIsTaken_ThenThrowLibraryAlreadyBookedException() {
        Reader readerTom = new Reader(4L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", new HashSet<>());
        Set<Reader> readerSet = new HashSet<>();
        readerSet.add(readerTom);
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, readerSet);
        Reader readerAlice = new Reader(6L, "WonderLand", "Alice",
                "alice@gmail.com", "0974443322", new HashSet<>());
        readerSet.add(readerAlice);
        Mockito.when(readerRepository.findById(6L)).thenReturn(Optional.of(readerAlice));
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Assertions.assertThatThrownBy(() -> readerService.takeBook(6L,
                1L)).isInstanceOf(LibraryAlreadyBookedException.class);
    }

    @Test
    void returnBook_WhenIsOk_ThenReturnReader() {
        Reader reader = new Reader(4L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", Collections.emptySet());
        Set<Reader> readerSet = new HashSet<>();
        readerSet.add(reader);
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, readerSet);
        Mockito.when(readerRepository.findById(reader.getId()))
                .thenReturn(Optional.of(reader));
        Mockito.when(bookRepository.findById(book.getBookId()))
                .thenReturn(Optional.of(book));
        Assertions.assertThat(readerService.returnBook(reader.getId(), book.getBookId()))
                .isEqualTo(reader);
    }

    @Test
    void returnBook_WhenBookNotExists_ThenThrowLibraryNotFoundException() {
        Mockito.when(bookRepository.findById(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.returnBook(4L, 5L))
                .isInstanceOf(LibraryNotFoundException.class);
    }

    @Test
    void returnBook_WhenReaderNotExists_ThenThrowLibraryNotFoundException() {
        Reader readerTom = new Reader(4L, "Soyer", "Tom", "tom@gmail.com",
                "0509999999", Collections.emptySet());
        Set<Reader> readerSet = new HashSet<>();
        readerSet.add(readerTom);
        Book book =  new Book(1L, "Jane Air", "Charlotte Bronte",
                456, readerSet);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(readerRepository.findById(5L))
                .thenThrow(LibraryNotFoundException.class);
        Assertions.assertThatThrownBy(() -> readerService.returnBook(5L,
                1L)).isInstanceOf(LibraryNotFoundException.class);
    }
}
