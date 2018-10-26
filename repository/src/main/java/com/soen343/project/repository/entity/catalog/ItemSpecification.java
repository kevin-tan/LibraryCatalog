package com.soen343.project.repository.entity.catalog;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soen343.project.database.base.DatabaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({@JsonSubTypes.Type(value = PrintedItem.class, name = "PrintedItem"), @JsonSubTypes.Type(value = MediaItem.class, name = "MediaItem")})
@NoArgsConstructor
public abstract class ItemSpecification implements DatabaseEntity {
    private long id;
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
