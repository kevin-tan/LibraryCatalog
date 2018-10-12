package com.soen343.project.repository.entity.catalog;

import lombok.Data;

import java.util.Objects;

@Data
public abstract class ItemSpecification {
    private long id;
    private String title;

    public ItemSpecification(long id, String title){
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemSpecification)) return false;
        if (!super.equals(o)) return false;
        ItemSpecification that = (ItemSpecification) o;
        return Objects.equals(title, that.title);
    }
}
