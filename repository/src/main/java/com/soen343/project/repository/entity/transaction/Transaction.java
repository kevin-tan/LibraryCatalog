package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.user.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public abstract class Transaction implements DatabaseEntity {
    private Long id;
    private Item item;
    private Client client;
    private Date transactionDate;

    public Transaction(Long id, Item item, Client client, Date transactionDate) {
        this.id = id;
        this.item = item;
        this.client = client;
        this.transactionDate = transactionDate;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = ITEMID + " = '" + item.getId() + "', ";
        columnValues += USERID + " = '" + client.getId() + "'";
        columnValues += TRANSACTIONDATE + " = '" + transactionDate + "'";
        return columnValues;
    }

    @JsonIgnore
    public String toSQLValue() {
        return "('" + item.getId() + "','" + client.getId() +  "','" + transactionDate + "','";
    }


    public Long getId() {
        return id;
    }


}