package com.soen343.project.repository.entity.catalog;

import lombok.Builder;

public class Music extends MediaItem{
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

    public boolean isEqual(Music music){
        return super.isEqual(music) && type.equals(music.type) && artist.equals(music.artist) && label.equals(music.label) &&
            asin.equals(music.asin);
    }
}
