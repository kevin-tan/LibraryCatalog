package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.soen343.project.repository.dao.transaction.com.DateConverter.DATE_FORMAT;
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

    public LoanTransaction(LoanableItem loanableItem, Client client, LocalDateTime transactionDate) {
        super(loanableItem, client, transactionDate);
        this.loanableItem.setAvailable(false);
        this.dueDate = this.transactionDate.plusDays(LoanDays.DAYS_UNTIL_DUE.get(loanableItem.getType()));
    }

    @Override
    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues =
                super.sqlUpdateValues() + ", " + DUEDATE + " = '" + dueDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + "'";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return super.toSQLValue() + "','" + dueDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + "')";
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