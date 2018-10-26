package com.soen343.project.repository.entity.catalog;

import lombok.Data;

import java.util.Objects;

@Data
public abstract class PrintedItem extends ItemSpecification {

    private String publisher;
    private String pubDate;
    private String language;
    private String isbn10;
    private String isbn13;

    public PrintedItem(long id, String title, String publisher, String pubDate,
                       String language, String isbn10, String isbn13){
        super(id, title);
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.language = language;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrintedItem)) return false;
        if (!super.equals(o)) return false;
        PrintedItem that = (PrintedItem) o;
        return Objects.equals(publisher, that.publisher) &&
                Objects.equals(pubDate, that.pubDate) &&
                Objects.equals(language, that.language) &&
                Objects.equals(isbn10, that.isbn10) &&
                Objects.equals(isbn13, that.isbn13);
    }
}
