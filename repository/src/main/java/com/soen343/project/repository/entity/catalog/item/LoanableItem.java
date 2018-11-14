package com.soen343.project.repository.entity.catalog.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;

import static com.soen343.project.repository.entity.EntityConstants.*;


public class LoanableItem extends Item{

    private String available;

    public LoanableItem(Long id, ItemSpecification spec, String available) {
        super(id, spec);
        this.available = available;
    }


    @Override
    public String sqlUpdateValues() {
        String columnValues = super.sqlUpdateValues() + (AVAILABLE + " = '" + available + "'");

        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + super.toSQLValue() + available + "')";
    }

}


