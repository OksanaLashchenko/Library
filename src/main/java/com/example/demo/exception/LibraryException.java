package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LibraryException {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;
}
