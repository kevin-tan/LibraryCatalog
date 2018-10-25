package com.soen343.project.repository.entity.catalog;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Music extends MediaItem {
    private String type;
    private String artist;
    private String label;
    private String asin;

    @Builder
    public Music(long id, String title, ZonedDateTime date,
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
}
