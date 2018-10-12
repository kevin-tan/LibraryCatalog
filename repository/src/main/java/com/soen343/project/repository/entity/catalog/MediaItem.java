package com.soen343.project.repository.entity.catalog;

public abstract class MediaItem extends ItemSpecification{

    //In the form 'Oct. 20 2009'
    private String releaseDate;

    public MediaItem(long id, String title, String releaseDate){
        super(id, title);
        this.releaseDate = releaseDate;
    }

    public boolean isEqual(MediaItem mediaItem){
        return super.isEqual(mediaItem) && releaseDate.equals(mediaItem.releaseDate);
    }
}
