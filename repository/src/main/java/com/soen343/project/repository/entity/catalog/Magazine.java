package com.soen343.project.repository.entity.catalog;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Magazine extends PrintedItem {

    @Builder
    public Magazine(long id, String title, String publisher, String pubDate, String lang,
                    String isbn10, String isbn13){
        super(id, title, publisher, pubDate, lang, isbn10, isbn13);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Magazine)) return false;
        return super.equals(o);
    }
}
