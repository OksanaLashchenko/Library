package com.example.demo.entity.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReaderDto {
    private String firstName;
    private String lastName;
    private Set<String> books;
}
