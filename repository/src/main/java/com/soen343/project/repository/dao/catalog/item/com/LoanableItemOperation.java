package com.soen343.project.repository.dao.catalog.item.com;

import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;

import java.sql.ResultSet;

import static com.soen343.project.database.query.QueryBuilder.createSaveQuery;
import static com.soen343.project.repository.entity.EntityConstants.ID;

/**
 * Created by Kevin Tan 2018-11-18
 */

public final class LoanableItemOperation {

    private LoanableItemOperation(){}

    public static DatabaseBatchOperation saveQuery(LoanableItem entity) {
        return statement -> {
            Item item = new Item(entity.getSpec());
            statement.executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue()));
            ResultSet rs = statement.executeQuery("SELECT id FROM Item ORDER BY id DESC LIMIT 1");
            if (rs.next()) entity.setId(rs.getLong(ID));
            statement.executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        };
    }
}
