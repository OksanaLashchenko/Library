package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.entity.dto.BookDto;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.service.BookService;
import com.example.demo.service.dto.mapping.BookMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookController.class})
class BookControllerTest {
    @MockBean
    private BookService bookService;
    @MockBean
    private BookMapper bookMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllBooks_WhenIsOk_ThenReturnListOfBookDto() throws Exception {
        Book book1 = new Book(1L,"Jane Air", "Charlotte Bronte",
                456, null);
        Book book2 = new Book(2L, "War and Peace", "Lev Tolstoy",
                968, null);
        Book book3 = new Book(3L, "Anna Karenina", "Lev Tolstoy",
                654, null);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        Mockito.when(bookService.findAllBooks()).thenReturn(books);

        BookDto bookDto1 = new BookDto("Jane Air", "Charlotte Bronte", null);
        BookDto bookDto2 = new BookDto("War and Peace", "Lev Tolstoy", null);
        BookDto bookDto3 = new BookDto("Anna Karenina", "Lev Tolstoy", null);
        List<BookDto> booksDto = new ArrayList<>();
        booksDto.add(bookDto1);
        booksDto.add(bookDto2);
        booksDto.add(bookDto3);

        String valueAsString = objectMapper.writeValueAsString(booksDto);

        Mockito.when(bookMapper.bookToBookDto(book1)).thenReturn(bookDto1);
        Mockito.when(bookMapper.bookToBookDto(book2)).thenReturn(bookDto2);
        Mockito.when(bookMapper.bookToBookDto(book3)).thenReturn(bookDto3);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(valueAsString));
    }

    @Test
    void findBookById_WhenIsOk_ThenReturnBook() throws Exception {
        Long bookId = 1L;
        Long readerId = 1L;
        Reader reader = new Reader(readerId, "Victoria", "Kharchenko", "kharchenko@gmail.com", "0501111111",
                Collections.emptySet());
        Set<Reader> readerSet = new HashSet<>();
        readerSet.add(reader);
        Book book = new Book(bookId,"Jane Air", "Charlotte Bronte",
                456, readerSet);
        Set<String> readerDtoSet = new HashSet<>();
        readerDtoSet.add(reader.getLastName());
        BookDto bookDto = new BookDto("Jane Air", "Charlotte Bronte", readerDtoSet);
        String valueAsString = objectMapper.writeValueAsString(bookDto);

        Mockito.when(bookMapper.bookToBookDto(book)).thenReturn(bookDto);
        Mockito.when(bookService.findBookById(bookId)).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().json(valueAsString));
    }

    @Test
    void findBookById_WhenBookNotFound_ThenReturn404Error() throws Exception {
        Long bookId = 1L;
        Mockito.when(bookService.findBookById(bookId)).thenThrow(LibraryNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBook_WhenArgumentNotValid_ThenReturn400Error() throws Exception {
        Book book = new Book(1L,"", "Charlotte Bronte",
                456, Collections.emptySet());
        String valueAsString = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(valueAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveBook_WhenIsOk_ThenReturnBook() throws Exception {
        Book book = new Book(1L, "Jane Air", "Charlotte Bronte",
                456, null);
        Mockito.when(bookService.findBookById(book.getBookId())).thenReturn(book);
        Mockito.when(bookService.saveBook(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        String valueAsString = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(valueAsString))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBook_WhenIsOk() throws Exception {
        Long bookId = 1L;
        Book book = new Book(1L, "Jane Air", "Charlotte Bronte",
                456, null);
        Mockito.when(bookService.findBookById(bookId)).thenReturn(book);
        Mockito.when(bookService.deleteBook(bookId)).thenReturn(bookId);
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBook_WhenBookIsNotFound_ThenThrowLibraryNotFoundException()
            throws Exception {
        Long bookId = 5L;
        Mockito.when(bookService.findBookById(bookId))
                .thenThrow(LibraryNotFoundException.class);
        Mockito.when(bookService.deleteBook(bookId)).thenThrow(LibraryNotFoundException.class);
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }
}
