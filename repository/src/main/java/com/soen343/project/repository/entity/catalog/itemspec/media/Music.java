package com.soen343.project.repository.entity.catalog.itemspec.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.itemspec.media.common.MediaItem;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Music extends MediaItem {
    private String type;
    private String artist;
    private String label;
    private String asin;

    @Builder
    public Music(long id, String title, String date,
                 String type, String artist, String label, String asin){
        super(id, title, date);
        this.type = type;
        this.artist = artist;
        this.label = label;
        this.asin = asin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Music)) return false;
        if (!super.equals(o)) return false;
        Music music = (Music) o;
        return Objects.equals(type, music.type) &&
                Objects.equals(artist, music.artist) &&
                Objects.equals(label, music.label) &&
                Objects.equals(asin, music.asin);
    }

    public static Music buildMusic(ResultSet rs) throws SQLException {
        return Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL))
                .type(rs.getString(TYPE)).build();
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = TITLE + " = '" + getTitle() + "', ";
        columnValues += RELEASEDATE + " = '" + getReleaseDate() + "', ";
        columnValues += TYPE + " = '" + type + "', ";
        columnValues += ARTIST + " = '" + artist + "', ";
        columnValues += LABEL + " = '" + label + "', ";
        columnValues += ASIN + " = '" + asin + "'";

        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + getTitle() + "','" + getReleaseDate() + "','" + type + "','" + artist + "','" + label + "','" +
                asin + "')";
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return MUSIC_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns(){
        return MUSIC_TABLE_WITH_COLUMNS;
    }
}
