package com.soen343.project.repository.entity.catalog.itemspec.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.itemspec.media.common.MediaItem;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Movie extends MediaItem {
    private String director;
    private List<String> producers;
    private List<String> actors;
    private List<String> dubbed;
    private String language;
    private String subtitles;
    private int runTime;

    @Builder
    public Movie(long id, String title, String date, String director, List<String> producers, List<String> actors, List<String> dubbed,
                 String lang, String subtitles, int runTime) {
        super(id, title, date);
        this.director = director;
        this.producers = producers;
        this.actors = actors;
        this.dubbed = dubbed;
        this.language = lang;
        this.subtitles = subtitles;
        this.runTime = runTime;
    }

    public static Movie buildMovie(ResultSet rs, List<String> actors, List<String> dubbed, List<String> producers) throws SQLException {
        return Movie.builder().id(rs.getLong(ID)).actors(actors).date(rs.getString(RELEASEDATE)).director(rs.getString(DIRECTOR))
                .dubbed(dubbed).lang(rs.getString(LANGUAGE)).producers(producers).runTime(rs.getInt(RUNTIME))
                .subtitles(rs.getString(SUBTITLES)).title(rs.getString(TITLE)).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(director, movie.director) && producers.equals(movie.producers) && actors.equals(movie.actors) &&
               dubbed.equals(movie.dubbed) && Objects.equals(language, movie.language) && Objects.equals(subtitles, movie.subtitles) &&
               runTime == movie.runTime;
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = TITLE + " = '" + getTitle() + "', ";
        columnValues += RELEASEDATE + " = '" + getReleaseDate() + "', ";
        columnValues += DIRECTOR + " = '" + director + "', ";
        columnValues += LANGUAGE + " = '" + language + "', ";
        columnValues += SUBTITLES + " = '" + subtitles + "', ";
        columnValues += RUNTIME + " = " + runTime + "";

        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + getTitle() + "','" + getReleaseDate() + "','" + director + "','" + language + "','" + subtitles + "'," + runTime +
               ")";
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return MOVIE_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return MOVIE_TABLE_WITH_COLUMNS;
    }

    @JsonIgnore
    public String getProducersTable() {
        return PRODUCERS_TABLE;
    }

    @JsonIgnore
    public String getActorsTable() {
        return ACTORS_TABLE;
    }

    @JsonIgnore
    public String getDubbedTable() {
        return DUBBED_TABLE;
    }

    @JsonIgnore
    public String getProducersTableWithColumns() {
        return PRODUCERS_TABLE_WITH_COLUMNS;
    }

    @JsonIgnore
    public String getActorsTableWithColumns() {
        return ACTORS_TABLE_WITH_COLUMNS;
    }

    @JsonIgnore
    public String getDubbedTableWithColumns() {
        return DUBBED_TABLE_WITH_COLUMNS;
    }

    @JsonIgnore
    public String getProducersSQLValues() {
        return listToSQLValues(this.producers);
    }

    @JsonIgnore
    public String getActorsSQLValues() {
        return listToSQLValues(this.actors);
    }

    @JsonIgnore
    public String getDubbedSQLValues() {
        return listToSQLValues(this.dubbed);
    }

    @JsonIgnore
    public String listToSQLValues(List<String> items) {
        StringBuilder values = new StringBuilder("");
        for (String item : items) {
            values.append("('" + this.getId() + "', '" + item + "'),");
        }
        return values.toString().substring(0, values.length() - 1);
    }

    @JsonIgnore
    public String listToSQLUpdateValues(List<String> items) {
        StringBuilder values = new StringBuilder("");
        for (String item : items) {
            values.append("('" + this.getId() + "', '" + item + "'),");
        }
        return values.toString().substring(0, values.length() - 1);
    }
}
