package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class LoanTransaction extends Transaction {

    private LocalDateTime dueDate;

    @Builder
    public LoanTransaction(Long id, LoanableItem loanableItem, Client client, LocalDateTime transactionDate, LocalDateTime dueDate) {
        super(id, loanableItem, client, transactionDate);
        this.dueDate = dueDate;
    }

    public LoanTransaction(LoanableItem loanableItem, Client client) {
        super(loanableItem, client);
        this.loanableItem.setAvailable(false);
        this.dueDate = this.transactionDate.plusDays(LoanDays.DAYS_UNTIL_DUE.get(loanableItem.getType()));
    }

    @Override
    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = super.sqlUpdateValues() + (DUEDATE + " = '" + dueDate + "'");
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return  super.toSQLValue() + dueDate + "')";
    }

    @Override
    @JsonIgnore
    public String getTable() { return LOANTRANSACTION_TABLE; }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return LOANTRANSACTION_TABLE_WITH_COLUMNS;
    }

}