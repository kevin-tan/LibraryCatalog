package com.soen343.project.repository.entity.catalog.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.user.Client;

import static com.soen343.project.repository.entity.EntityConstants.AVAILABLE;


public class LoanableItem extends Item {

    private Boolean available;
    private Client client;

    public LoanableItem(Long id, ItemSpecification spec, Boolean available, Client client) {
        super(id, spec);
        this.available = available;
        this.client = client;
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


