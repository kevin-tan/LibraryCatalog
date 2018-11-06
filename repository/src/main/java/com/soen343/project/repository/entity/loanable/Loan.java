package com.soen343.project.repository.entity.loanable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.item.Item;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Loan implements DatabaseEntity {
    private Long id;
    private Item item;
    private String loanTime;
    private String checkoutDate;
    private String dueDate;

    @Builder
    public Loan(Long id, Item item, String loanTime, String checkoutDate, String dueDate) {
        this.id = id;
        this.item = item;
        this.loanTime = loanTime;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = ITEMID + " = '" + item.getId() + "', ";
        columnValues += LOANTIME + " = '" + loanTime + "'";
        columnValues += CHECKOUTDATE + " = '" + checkoutDate + "'";
        columnValues += DUEDATE + " = '" + dueDate + "'";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + item.getId() + "','" + loanTime +  "','"+ checkoutDate +  "','" + dueDate + "')";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getTable() { return LOAN_TABLE; }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return LOAN_TABLE_WITH_COLUMNS;
    }

}