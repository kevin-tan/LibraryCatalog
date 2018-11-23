package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.media.common.MediaItem;
import com.soen343.project.repository.entity.catalog.itemspec.printed.common.PrintedItem;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.soen343.project.repository.dao.transaction.com.DateConverter.DATE_FORMAT;
import static com.soen343.project.repository.entity.EntityConstants.*;
import static org.valid4j.Assertive.*;

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

        ensure(this.loanableItem.getSpec() instanceof PrintedItem ? daysDifference(dueDate, transactionDate) == PrintedItem.DAYS_UNTIL_DUE : true);
        ensure(this.loanableItem.getSpec() instanceof MediaItem ? daysDifference(dueDate, transactionDate) == MediaItem.DAYS_UNTIL_DUE : true);
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

    private static long daysDifference(LocalDateTime d1, LocalDateTime d2) {
        return ChronoUnit.DAYS.between(d2, d1);
    }

}