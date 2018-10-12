package com.soen343.project.repository.entity.catalog;

import lombok.Builder;

public class Book extends PrintedItem{

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

    public boolean isEqual(Book book) {
        return super.isEqual(book) && author.equals(book.author) && format.equals(book.format) && pages == book.pages;
    }
}
