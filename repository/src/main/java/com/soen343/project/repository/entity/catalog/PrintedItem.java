package com.soen343.project.repository.entity.catalog;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@JsonSubTypes({@JsonSubTypes.Type(value = Book.class, name = "Book"), @JsonSubTypes.Type(value = Magazine.class, name = "Magazine")})
@NoArgsConstructor
public abstract class PrintedItem extends ItemSpecification {

    private String publisher;
    private String pubDate;
    private String lang;
    private String isbn10;
    private String isbn13;

    public PrintedItem(long id, String title, String publisher, String pubDate,
                       String lang, String isbn10, String isbn13){
        super(id, title);
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.lang = lang;
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
                Objects.equals(lang, that.lang) &&
                Objects.equals(isbn10, that.isbn10) &&
                Objects.equals(isbn13, that.isbn13);
    }
}
