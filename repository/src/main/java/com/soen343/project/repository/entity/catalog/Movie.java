package com.soen343.project.repository.entity.catalog;

import lombok.Builder;

public class Movie extends MediaItem{
    private String director;
    private String producer;
    private String actor;
    private String lang;
    private String subtitles;
    private String runTime;

    @Builder
    public Movie(long id, String title, String date, String director, String producer, String actor
            , String lang, String subtitles, String runTime){
        super(id, title, date);
        this.director = director;
        this.producer = producer;
        this.actor = actor;
        this.lang = lang;
        this.subtitles = subtitles;
        this.runTime = runTime;
    }

    public boolean isEqual(Movie movie){
        return super.isEqual(movie) && director.equals(movie.director) && producer.equals(movie.producer) &&
                actor.equals(movie.actor) && lang.equals(movie.lang) && subtitles.equals(movie.subtitles) &&
                runTime.equals(movie.runTime);
    }
}
