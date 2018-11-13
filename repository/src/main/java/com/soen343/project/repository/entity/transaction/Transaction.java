package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.loanable.Loanable;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Transaction implements DatabaseEntity {
    private Long id;
    private Loanable loanable;
    private Client client;
    private String transactionType;

    @Builder
    public Transaction(Long id, Loanable loanable, Client client, String transactionType) {
        this.id = id;
        this.loanable = loanable;
        this.client = client;
        this.transactionType = transactionType;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = LOANABLEID + " = '" + loanable.getId() + "', ";
        columnValues += USERID + " = '" + client.getId() + "'";
        columnValues += TRANSACTIONTYPE + " = '" + transactionType + "'";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + loanable.getId() + "','" + client.getId() +  "','" + transactionType + "')";
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