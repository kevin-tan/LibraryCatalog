package com.soen343.project.repository.entity.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Book extends PrintedItem {

    private String author;
    private Format format;
    private int pages;

    public enum Format {
        PAPERBACK, HARDCOVER;
    }

    @Builder
    public Book(long id, String title, String publisher, String pubDate, String lang, String isbn10,
                String isbn13, String author, Format format, int pages){
        super(id, title, publisher, pubDate, lang, isbn10, isbn13);
        this.author = author;
        this.format = format;
        this.pages = pages;
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = TITLE + " = '" + getTitle() + "', ";
        columnValues += PUBLISHER + " = '" + getPublisher() + "', ";
        columnValues += PUBDATE + " = '" + getPubDate() + "', ";
        columnValues += LANGUAGE + " = '" + getLanguage() + "', ";
        columnValues += ISBN10 + " = '" + getIsbn10() + "', ";
        columnValues += ISBN13 + " = '" + getIsbn13() + "'";
        columnValues += AUTHOR + " = '" + author + "'";
        columnValues += FORMAT + " = '" + format.name() + "'";
        columnValues += PAGES + " = '" + pages + "'";

        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + getTitle() + "','" + getPublisher() + "','" + getPubDate() + "','" + getLanguage() + "','" + getIsbn10() + "','" +
                getIsbn13() + "','" + author + "','" + format.name() + "','" + pages + "')";
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return BOOK_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns(){
        return BOOK_TABLE_WITH_COLUMNS;
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
