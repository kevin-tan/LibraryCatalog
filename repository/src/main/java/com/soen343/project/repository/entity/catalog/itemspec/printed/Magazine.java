package com.soen343.project.repository.entity.catalog.itemspec.printed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.itemspec.printed.common.PrintedItem;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Magazine extends PrintedItem {

    @Builder
    public Magazine(long id, String title, String publisher, String pubDate, String lang, String isbn10, String isbn13) {
        super(id, title, publisher, pubDate, lang, isbn10, isbn13);
    }

    public static Magazine buildMagazine(ResultSet rs) throws SQLException {
        return Magazine.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10)).isbn13(rs.getString(ISBN13))
                .lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE)).publisher(rs.getString(PUBLISHER)).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Magazine)) return false;
        return super.equals(o);
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = TITLE + " = '" + getTitle() + "', ";
        columnValues += PUBLISHER + " = '" + getPublisher() + "', ";
        columnValues += PUBDATE + " = '" + getPubDate() + "', ";
        columnValues += LANGUAGE + " = '" + getLanguage() + "', ";
        columnValues += ISBN10 + " = '" + getIsbn10() + "', ";
        columnValues += ISBN13 + " = '" + getIsbn13() + "'";

        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + getTitle() + "','" + getPublisher() + "','" + getPubDate() + "','" + getLanguage() + "','" + getIsbn10() + "','" +
               getIsbn13() + "')";
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return MAGAZINE_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return MAGAZINE_TABLE_WITH_COLUMNS;
    }
}
