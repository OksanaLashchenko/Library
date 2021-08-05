package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    private Long id;
    private String lastName;
    private String firstName;
    private List<Book> books = new ArrayList<>();
}
