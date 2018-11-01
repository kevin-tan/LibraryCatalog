package com.soen343.project.repository.entity.catalog;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@JsonSubTypes({@JsonSubTypes.Type(value = Movie.class, name = "Movie"), @JsonSubTypes.Type(value = Music.class, name = "Music")})
@NoArgsConstructor
public abstract class MediaItem extends ItemSpecification {

    //In the form 'Oct 20 2009'
    private String releaseDate;

    public MediaItem(long id, String title, String releaseDate){
        super(id, title);
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaItem)) return false;
        if (!super.equals(o)) return false;
        MediaItem mediaItem = (MediaItem) o;
        return Objects.equals(releaseDate, mediaItem.releaseDate);
    }
}
