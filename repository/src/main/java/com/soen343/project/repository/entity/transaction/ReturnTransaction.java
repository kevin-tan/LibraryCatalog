package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.soen343.project.repository.entity.EntityConstants.RETURNTRANSACTION_TABLE;
import static com.soen343.project.repository.entity.EntityConstants.RETURNTRANSACTION_TABLE_WITH_COLUMNS;

@Data
@NoArgsConstructor
public class ReturnTransaction extends Transaction {

    @Builder
    public ReturnTransaction(Long id, Item item, Client client, Date transactionDate) {
        super(id, item, client, transactionDate);

    }

    @Override
    @JsonIgnore
    public String getTable() { return RETURNTRANSACTION_TABLE; }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return RETURNTRANSACTION_TABLE_WITH_COLUMNS;
    }
}
