package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.soen343.project.repository.entity.EntityConstants.RETURNTRANSACTION_TABLE;
import static com.soen343.project.repository.entity.EntityConstants.RETURNTRANSACTION_TABLE_WITH_COLUMNS;

@Data
@NoArgsConstructor
public class ReturnTransaction extends Transaction {

    @Builder
    public ReturnTransaction(Long id, LoanableItem loanableItem, Client client, LocalDateTime transactionDate) {
        super(id, loanableItem, client, transactionDate);
    }

    public ReturnTransaction(LoanableItem loanableItem, Client client, LocalDateTime transactionDate) {
        super(loanableItem, client, transactionDate);
    }

    @Override
    @JsonIgnore
    public String sqlUpdateValues() {
        return super.sqlUpdateValues() + "')";
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return super.toSQLValue() + "')";
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