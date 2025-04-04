package com.example.demo.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long bookId;
    private String title;
    private String author;
    private int pages;

}
