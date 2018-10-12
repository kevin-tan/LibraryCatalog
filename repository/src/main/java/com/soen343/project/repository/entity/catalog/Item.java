package com.soen343.project.repository.entity.catalog;

public class Item {
    private long id;
    private ItemSpecification spec;

    public Item(long id, ItemSpecification spec){
        this.id = id;
        this.spec = spec;
    }

    public void setSpec(ItemSpecification spec) {
        this.spec = spec;
    }

    public long getId() {
        return id;
    }

    public ItemSpecification getSpec() {
        return spec;
    }
}
