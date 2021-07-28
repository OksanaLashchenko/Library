package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Book {
    private Long bookId;
    private Reader reader;
    private String title;
    private String author;
    private int pages;
}
