package com.soen343.project.repository.entity.catalog;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Movie extends MediaItem {
    private String director;
    private String producer;
    private String actor;
    private String lang;
    private String subtitles;
    private String runTime;

    @Builder
    public Movie(long id, String title, ZonedDateTime date, String director, String producer, String actor
            , String lang, String subtitles, String runTime){
        super(id, title, date);
        this.director = director;
        this.producer = producer;
        this.actor = actor;
        this.lang = lang;
        this.subtitles = subtitles;
        this.runTime = runTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(director, movie.director) &&
                Objects.equals(producer, movie.producer) &&
                Objects.equals(actor, movie.actor) &&
                Objects.equals(lang, movie.lang) &&
                Objects.equals(subtitles, movie.subtitles) &&
                Objects.equals(runTime, movie.runTime);
    }
}
