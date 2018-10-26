package com.soen343.project.repository.entity.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import lombok.Builder;
import lombok.Data;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
public class Item implements DatabaseEntity {
    private Long id;
    private ItemSpecification spec;

    @Builder
    public Item(Long id, ItemSpecification spec){
        this.id = id;
        this.spec = spec;
    }

    public Item(ItemSpecification spec) {
        this.spec = spec;
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = ITEMSPECID + " = '" + spec.getId() + "', ";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + spec.getId() + "')";
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return ITEMSPEC_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns(){
        return ITEMSPEC_TABLE_WITH_COLUMNS;
    }

    @Override
    public Long getId() {
        return id;
    }
}
