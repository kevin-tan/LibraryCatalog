package com.soen343.project.repository.entity.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soen343.project.database.base.DatabaseEntity;
import lombok.Data;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Item implements DatabaseEntity {
    private Long id;
    private final ItemSpecification spec;
    private final String type;

    public Item(Long id, ItemSpecification spec){
        this.id = id;
        this.spec = spec;
        this.type = spec.getClass().getSimpleName();
    }

    public Item(ItemSpecification spec){
        this.spec = spec;
        this.type = spec.getClass().getSimpleName();
    }


    @Override
    public String sqlUpdateValues() {
        String columnValues = ITEMSPECID + " = '" + spec.getId() + "', ";
        columnValues += TYPE + " = '" + type + "'";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + spec.getId() + "','" + type + "')";
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return ITEM_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns(){
        return ITEM_TABLE_WITH_COLUMNS;
    }

    @Override
    public Long getId() {
        return id;
    }
}
