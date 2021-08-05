package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryExceptionHandler {
    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(value = {LibraryNotFoundException.class})
    public ResponseEntity<LibraryException> handleLibraryNotFoundException(LibraryNotFoundException e) {
        LibraryException libraryException = new LibraryException(
                e.getMessage(),
                NOT_FOUND,
                LocalDateTime.now());
        return new ResponseEntity<>(libraryException, NOT_FOUND);
    }

    @ExceptionHandler(value = {LibraryAlreadyBookedException.class})
    public ResponseEntity<Object> handleLibraryAlreadyBookedException(
            LibraryAlreadyBookedException e) {
        LibraryException libraryException = new LibraryException(
                e.getMessage(),
                BAD_REQUEST,
                LocalDateTime.now());
        return new ResponseEntity<>(libraryException,BAD_REQUEST);
    }
}
