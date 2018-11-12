package com.soen343.project.repository.entity.loanable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.item.Item;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class Loanable implements DatabaseEntity {
    private Long id;
    private Item item;
    private Date checkoutDate;
    private Date dueDate;

    @Builder
    public Loanable(Long id, Item item, Date checkoutDate, Date dueDate) {
        this.id = id;
        this.item = item;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    @JsonIgnore
    public String sqlUpdateValues() {
        String columnValues = ITEMID + " = '" + item.getId() + "', ";
        columnValues += CHECKOUTDATE + " = '" + checkoutDate + "'";
        columnValues += DUEDATE + " = '" + dueDate + "'";
        return columnValues;
    }

    @Override
    @JsonIgnore
    public String toSQLValue() {
        return "('" + item.getId() + "','" + checkoutDate +  "','" + dueDate + "')";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getTable() { return LOANABLE_TABLE; }

    @Override
    @JsonIgnore
    public String getTableWithColumns() {
        return LOANABLE_TABLE_WITH_COLUMNS;
    }

}