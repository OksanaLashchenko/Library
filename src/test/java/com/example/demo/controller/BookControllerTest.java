package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllBooks_WhenIsOk_ThenReturnListOfBooks() throws Exception {
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

        String valueAsString = objectMapper.writeValueAsString(books);
        when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(valueAsString));
    }

    @Test
    void findBookById_WhenIsOk_ThenReturnBook() throws Exception {
        Long bookId = 1L;
        Reader reader = new Reader(1L, "Victoria", "Kharchenko",
                Collections.emptyList());
        Book book = new Book(1L,"Jane Air", "Charlotte Bronte",
                456, reader);
        String valueAsString = objectMapper.writeValueAsString(book);
        when(bookService.findBookById(bookId)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().json(valueAsString));
    }

    @Test
    void findBookById_WhenBookNotFound_ThenReturn404Error() throws Exception {
        Long bookId = 1L;
        when(bookService.findBookById(bookId)).thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBook_WhenIsOk_ThenReturnBook() throws Exception {
        Book book = new Book(1L, "Jane Air", "Charlotte Bronte",
                456, null);
        when(bookService.findBookById(book.getBookId())).thenReturn(book);
        when(bookService.saveBook(any(Book.class)))
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
        when(bookService.findBookById(bookId)).thenReturn(book);
        when(bookService.deleteBook(bookId)).thenReturn(bookId);

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBook_WhenBookIsNotFound_ThenThrowLibraryNotFoundException()
            throws Exception {
        Long bookId = 5L;
        when(bookService.findBookById(bookId))
                .thenThrow(LibraryNotFoundException.class);
        when(bookService.deleteBook(bookId)).thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }

}
