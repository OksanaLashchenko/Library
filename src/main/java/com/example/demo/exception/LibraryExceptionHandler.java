package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * LibraryExceptionHandler is a class responsible for handling specific exceptions
 * which is peculiar to this Library Application.
 * It includes managing specific NotFound Exception thrown by some methods,
 * as well as LibraryAlreadyBookedException - connected with business logic of application,
 * when book is already taken and is unavailable for taking by someone else.
 */
@ControllerAdvice
public class LibraryExceptionHandler {
    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    /**
     * @param exception is specific LibraryNotFoundException designed for managing
     *                  cases when an entity inside the Library App is not found
     * @return ResponseEntity in a form of LibraryException.class
     */
    @ExceptionHandler(value = {LibraryNotFoundException.class})
    public ResponseEntity<LibraryException> handleLibraryNotFoundException(
            LibraryNotFoundException exception) {
        LibraryException libraryException = new LibraryException(
                exception.getMessage(),
                NOT_FOUND,
                LocalDateTime.now());
        return new ResponseEntity<>(libraryException, NOT_FOUND);
    }

    /**
     * @param exception is specific LibraryAlreadyBookedException designed for managing
     *                  cases when book is already taken and is unavailable for
     *                  taking by someone else
     * @return ResponseEntity in a form of LibraryException.class
     */
    @ExceptionHandler(value = {LibraryAlreadyBookedException.class})
    public ResponseEntity<Object> handleLibraryAlreadyBookedException(
            LibraryAlreadyBookedException exception) {
        LibraryException libraryException = new LibraryException(
                exception.getMessage(),
                BAD_REQUEST,
                LocalDateTime.now());
        return new ResponseEntity<>(libraryException,BAD_REQUEST);
    }
}
