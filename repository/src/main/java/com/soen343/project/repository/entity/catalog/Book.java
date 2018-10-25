package com.soen343.project.repository.entity.catalog;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Book extends PrintedItem {

    private String author;
    private String format;
    private int pages;

    @Builder
    public Book(long id, String title, String publisher, String pubDate, String lang, String isbn10,
                String isbn13, String author, String format, int pages){
        super(id, title, publisher, pubDate, lang, isbn10, isbn13);
        this.author = author;
        this.format = format;
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return pages == book.pages &&
                Objects.equals(author, book.author) &&
                Objects.equals(format, book.format);
    }
}
