package com.soen343.project.repository.entity.catalog;

import lombok.Data;

@Data
public class Item {
    private long id;
    private ItemSpecification spec;

    public Item(long id, ItemSpecification spec){
        this.id = id;
        this.spec = spec;
    }
}
