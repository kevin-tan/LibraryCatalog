package com.soen343.project.repository.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
@NoArgsConstructor
public class LoanTransaction extends Transaction {

    private Date dueDate;

    @Builder
    public LoanTransaction(Long id, Client client, Item item, Date checkoutDate, Date dueDate) {
        super(id, client, item, checkoutDate);
        this.dueDate = dueDate;

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
        return "('" + super.toSQLValue() + dueDate + "')";
    }

}