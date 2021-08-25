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
import com.example.demo.entity.dto.ReaderDto;


@Mapper(componentModel = "spring")
public interface ReaderMapper {
    ReaderMapper READER_INSTANCE = Mappers.getMapper(ReaderMapper.class);

    @Mapping(source = "books", target = "books", qualifiedByName = "mapBooks")
    ReaderDto readerToReaderDto(Reader reader);

    @Named("mapBooks")
    default Set<String> mapBooks(Set<Book> source) {
        if (CollectionUtils.isEmpty(source)) {
            return new HashSet<>();
        }
        return source.stream().map(Book::getTitle)
                .collect(Collectors.toSet());
    }
}
