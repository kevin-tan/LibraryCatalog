package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public abstract class Transaction implements DatabaseEntity {
    private Long id;
    private Client client;
    private Item item;
    private Date checkoutDate;

    @Builder
    public Transaction(Long id, Client client, Item item, Date checkoutDate) {
        this.id = id;
        this.client = client;
        this.item = item;
        this.checkoutDate = checkoutDate;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = ITEMID + " = '" + item.getId() + "', ";
        columnValues += USERID + " = '" + client.getId() + "'";
        columnValues += CHECKOUTDATE + " = '" + checkoutDate + "'";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + item.getId() + "','" + client.getId() +  "','" + checkoutDate + "','" +
                getClass().getSimpleName() + "')";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getTable() { return TRANSAC_TABLE; }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return TRANSAC_TABLE_WITH_COLUMNS;
    }

}