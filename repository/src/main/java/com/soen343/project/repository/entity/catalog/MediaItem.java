package com.soen343.project.repository.entity.catalog;

import java.time.ZonedDateTime;
import java.util.Objects;

public abstract class MediaItem extends ItemSpecification {

    //In the form 'Oct. 20 2009'
    private ZonedDateTime releaseDate;

    public MediaItem(long id, String title, ZonedDateTime releaseDate){
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
