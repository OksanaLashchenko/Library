package com.example.demo.service.dto.mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reader;
import com.example.demo.entity.dto.BookDto;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper BOOK_INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "title", target = "bookTitle")
    @Mapping(source = "readers", target = "readers", qualifiedByName = "mapReaders")
    BookDto bookToBookDto(Book book);

    @Named("mapReaders")
    default Set<String> mapReaders(Set<Reader> source) {
        if (CollectionUtils.isEmpty(source)) {
            return new HashSet<>();
        }
        return source.stream().map(Reader::getLastName)
                .collect(Collectors.toSet());
    }
}
