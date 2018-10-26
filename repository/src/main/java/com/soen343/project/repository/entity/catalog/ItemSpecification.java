package com.soen343.project.repository.entity.catalog;

import com.soen343.project.database.base.DatabaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Data
public abstract class ItemSpecification implements DatabaseEntity {
    private Long id;
    private String title;

    public ItemSpecification(Long id, String title){
        this.id = id;
        this.title = title;
    }

    @Override
    public Long getId() {
        return id;
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
