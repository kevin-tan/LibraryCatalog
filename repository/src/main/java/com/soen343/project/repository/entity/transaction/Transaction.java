package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.soen343.project.repository.dao.transaction.com.DateConverter.DATE_FORMAT;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public abstract class Transaction implements DatabaseEntity {
    private Long id;
    protected LoanableItem loanableItem;
    protected Client client;
    protected LocalDateTime transactionDate;

    Transaction(Long id, LoanableItem loanableItem, Client client, LocalDateTime transactionDate) {
        this.id = id;
        this.loanableItem = loanableItem;
        this.client = client;
        this.transactionDate = transactionDate;
    }

    Transaction(LoanableItem loanableItem, Client client, LocalDateTime transactionDate) {
        loanableItem.setClient(client);
        this.loanableItem = loanableItem;
        this.client = client;
        this.transactionDate = transactionDate;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = ITEMID + " = " + loanableItem.getId() + ", ";
        columnValues += USERID + " = " + client.getId() + ", ";
        columnValues += TRANSACTIONDATE + " = '" + transactionDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + "'";
        return columnValues;
    }

    @JsonIgnore
    public String toSQLValue() {
        return "(" + loanableItem.getId() + "," + client.getId() +  ",'" + transactionDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }


    public Long getId() {
        return id;
    }


}