package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public abstract class Transaction implements DatabaseEntity {
    private Long id;
    private LoanableItem loanableItem;
    private Client client;
    private LocalDateTime transactionDate;

    public Transaction(Long id, LoanableItem loanableItem, Client client, LocalDateTime transactionDate) {
        this.id = id;
        this.loanableItem = loanableItem;
        this.client = client;
        this.transactionDate = transactionDate;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = ITEMID + " = " + loanableItem.getId() + ", ";
        columnValues += USERID + " = " + client.getId() + ", ";
        columnValues += TRANSACTIONDATE + " = '" + transactionDate + "'";
        return columnValues;
    }

    @JsonIgnore
    public String toSQLValue() {
        return "(" + loanableItem.getId() + "," + client.getId() +  ",'" + transactionDate;
    }


    public Long getId() {
        return id;
    }


}