package com.soen343.project.repository.entity.catalog.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.user.Client;
import lombok.Data;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
public class LoanableItem extends Item {

    private Boolean available;
    private Client client;

    public LoanableItem() {}

    public LoanableItem(Long id, ItemSpecification spec, Boolean available, Client client) {
        super(id, spec);
        this.available = available;
        this.client = client;
    }

    @Override
    public String sqlUpdateValues() {
        Long client_id = (client == null) ? null : client.getId();
        String columnValues = USERID + " = '" + client_id + "', ";
        columnValues += AVAILABLE + " = " + available + "";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + this.getId() + "','" + (client == null ? "NULL" : client.getId()) + "','" + available + "')";
    }

    @Override
    @JsonIgnore
    public String getTable() {
        return LOANABLEITEM_TABLE;
    }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return LOANABLEITEM_TABLE_WITH_COLUMNS;
    }

    @Override
    public Long getId() {
        return super.getId();
    }
}


