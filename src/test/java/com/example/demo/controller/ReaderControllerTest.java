package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.entity.Reader;
import com.example.demo.exception.LibraryAlreadyBookedException;
import com.example.demo.exception.LibraryNotFoundException;
import com.example.demo.service.BookService;
import com.example.demo.service.ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReaderController.class)
class ReaderControllerTest {

    @MockBean
    ReaderService readerService;
    @MockBean
    BookService bookService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findReader_WhenIsOk_ThemReturnReader() throws Exception {
        Long readerId = 1L;
        Reader reader = new Reader(1L, "Kharchenko", "Victoria",
                Collections.emptyList());
        String valueAsString = objectMapper.writeValueAsString(reader);
        when(readerService.findReader(readerId)).thenReturn(reader);

        mockMvc.perform(MockMvcRequestBuilders.get("/readers/{id}", readerId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(valueAsString));
    }

    @Test
    void findReader_WhenReaderNotFound_ThenReturn404Error() throws Exception {
        Long readerId = 1L;
        when(readerService.findReader(readerId))
                .thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/readers/{id}", readerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveReader_WhenIsOk_ThenReturnReader() throws Exception {
        Reader reader = new Reader(1L, "Kharchenko", "Victoria",
                Collections.emptyList());
        String valueAsString = objectMapper.writeValueAsString(reader);
        when(readerService.findReader(reader.getId())).thenReturn(reader);
        when(readerService.saveReader(any(Reader.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/readers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(valueAsString))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReader_WhenIsOk() throws Exception {
        Long readerId = 1L;
        Reader reader = new Reader(1L, "Kharchenko", "Victoria",
                Collections.emptyList());
        when(readerService.findReader(readerId)).thenReturn(reader);
        when(readerService.deleteReader(readerId)).thenReturn(readerId);

        mockMvc.perform(delete("/readers/{id}", readerId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReader_WhenReaderIsNotFound_ThenThrowLibraryNotFoundException()
            throws Exception {
        Long readerId = 5L;
        when(readerService.findReader(readerId))
                .thenThrow(LibraryNotFoundException.class);
        when(readerService.deleteReader(readerId))
                .thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(delete("/readers/{id}", readerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void takeBook_WhenIsOk() throws Exception {
        Long readerId = 1L;
        Long bookId = 1L;
        Reader reader = new Reader(1L, "Kharchenko", "Victoria",
                Collections.emptyList());
        String valueAsString = objectMapper.writeValueAsString(reader);
        when(readerService.takeBook(readerId, bookId)).thenReturn(reader);

        mockMvc.perform(post("/readers/{readerId}/books/{bookId}", readerId, bookId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(valueAsString))
                .andExpect(status().isOk());
    }

    @Test
    void takeBook_WhenBookNotFound_ThenThrowLibraryNotFoundException() throws Exception {
        Long readerId = 1L;
        Long bookId = 5L;
        when(bookService.findBookById(bookId))
                .thenThrow(LibraryNotFoundException.class);
        when(readerService.takeBook(readerId,bookId))
                .thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(post("/readers/{readerId}/books/{bookId}", readerId, bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void takeBook_WhenReaderNotFound_ThenThrowLibraryNotFoundException() throws Exception {
        Long readerId = 5L;
        Long bookId = 1L;
        when(readerService.findReader(readerId))
                .thenThrow(LibraryNotFoundException.class);
        when(readerService.takeBook(readerId, bookId))
                .thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(post("/readers/{readerId}/books/{bookId}", readerId, bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void takeBook_WhenBookIsNotAvailable_ThenThrowLibraryAlreadyBookedException()
            throws Exception {
        Long readerId = 1L;
        Long bookId = 1L;
        when(readerService.takeBook(readerId, bookId))
                .thenThrow(LibraryAlreadyBookedException.class);

        mockMvc.perform(post("/readers/{readerId}/books/{bookId}", readerId, bookId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBook_WhenIsOk() throws Exception {
        Long readerId = 1L;
        Long bookId = 1L;
        Reader reader = new Reader(1L, "Kharchenko", "Victoria",
                Collections.emptyList());
        String valueAsString = objectMapper.writeValueAsString(reader);
        when(readerService.returnBook(readerId, bookId)).thenReturn(reader);

        mockMvc.perform(post("/readers/return/{readerId}/books/{bookId}",
                readerId, bookId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(valueAsString))
                .andExpect(status().isOk());
    }

    @Test
    void returnBook_WhenBookNotFound_ThenThrowLibraryNotFoundException() throws Exception {
        Long readerId = 1L;
        Long bookId = 5L;
        when(bookService.findBookById(bookId))
                .thenThrow(LibraryNotFoundException.class);
        when(readerService.returnBook(readerId,bookId))
                .thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(post("/readers/return/{readerId}/books/{bookId}",
                readerId, bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnBook_WhenReaderNotFound_ThenThrowLibraryNotFoundException() throws Exception {
        Long readerId = 5L;
        Long bookId = 1L;
        when(readerService.findReader(readerId))
                .thenThrow(LibraryNotFoundException.class);
        when(readerService.returnBook(readerId, bookId))
                .thenThrow(LibraryNotFoundException.class);

        mockMvc.perform(post("/readers/return/{readerId}/books/{bookId}",
                readerId, bookId))
                .andExpect(status().isNotFound());
    }

}
