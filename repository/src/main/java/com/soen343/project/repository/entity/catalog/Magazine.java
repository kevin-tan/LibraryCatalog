package com.soen343.project.repository.entity.catalog;

import lombok.Builder;

public class Magazine extends PrintedItem{

    @Builder
    public Magazine(long id, String title, String publisher, String pubDate, String lang,
                    String isbn10, String isbn13){
        super(id, title, publisher, pubDate, lang, isbn10, isbn13);
    }

    public boolean isEqual(Magazine mag){
        return super.isEqual(mag);
    }
}
