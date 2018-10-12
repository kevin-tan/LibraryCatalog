package com.soen343.project.repository.entity.catalog;

public abstract class ItemSpecification {
    private long id;
    private String title;

    public ItemSpecification(long id, String title){
        this.id = id;
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean isEqual(ItemSpecification spec){
        return title.equals(spec.title);
    }
}
