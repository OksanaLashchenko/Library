package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reader {
    private Long id;
    private String lastName;
    private String firstName;
    private List<Book> books = new ArrayList<>();

    public Reader() {

    }

    public Reader(Long id) {
        this.id = id;
    }
}
