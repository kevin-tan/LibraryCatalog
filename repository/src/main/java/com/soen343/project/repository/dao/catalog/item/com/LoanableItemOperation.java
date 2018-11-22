package com.soen343.project.repository.dao.catalog.item.com;

import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.user.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.getItemSpec;
import static com.soen343.project.repository.entity.EntityConstants.*;
import static com.soen343.project.repository.entity.EntityConstants.PASSWORD;

/**
 * Created by Kevin Tan 2018-11-18
 */

public final class LoanableItemOperation {

    private LoanableItemOperation() {}

    public static DatabaseBatchOperation saveQuery(LoanableItem entity) {
        return statement -> loanableSaveOperation(statement, entity);
    }

    public static void loanableSaveOperation(Statement statement, LoanableItem entity) throws SQLException {
        Item item = new Item(entity.getSpec());
        statement.executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue()));
        ResultSet rs = statement.executeQuery(createFindIdOfLastAddedItem(ITEM_TABLE));
        if (rs.next()) entity.setId(rs.getLong(ID));
        statement.executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    public static DatabaseBatchOperation deleteQuery(LoanableItem item) {
        return statement -> {
            statement.execute(createDeleteQuery(ITEM_TABLE, item.getId()));
            statement.execute(createDeleteQuery(item.getTable(), item.getId()));
        };
    }

    public static String queryLoanableAndItemByItemspecType(String itemType, Long itemSpecId) {
        return "SELECT LoanableItem.* FROM LoanableItem, Item WHERE Item.itemSpecId = " + itemSpecId + " and Item.type = '" + itemType +
               "' and LoanableItem.id = Item.id";
    }

    public static String queryLoanableAndItemByItemspecType(ItemSpecification itemSpecification) {
        return queryLoanableAndItemByItemspecType(itemSpecification.getTable(), itemSpecification.getId());
    }

    public static DatabaseQueryOperation findAllLoanables() {
        return (rs, statement) -> {
            List<LoanableItem> loanableItems = new ArrayList<>();

            while (rs.next()) {
                Client client = (rs.getLong(USERID) == 0) ? null : new Client(rs.getLong(USERID));
                loanableItems.add(new LoanableItem(rs.getLong(ID), null, rs.getString(AVAILABLE)
                        .equals(BOOL_VAL_TRUE), client));
            }

            for (LoanableItem loanableItem : loanableItems) {
                Long itemSpecId = null;
                String itemSpecType = null;

                ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, loanableItem.getId()));
                if(itemRS.next()) {
                    itemSpecId = itemRS.getLong(ITEMSPECID);
                    itemSpecType = itemRS.getString(TYPE);
                }

                ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
                while (itemSpecRS.next()) {
                    loanableItem.setSpec(getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId));
                }

                if (loanableItem.getClient() != null) {
                    ResultSet clientRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, loanableItem.getClient()
                            .getId()));
                    while (clientRS.next()) {
                        loanableItem.setClient(
                                new Client(clientRS.getLong(ID), clientRS.getString(FIRST_NAME), clientRS.getString(LAST_NAME),
                                        clientRS.getString(PHYSICAL_ADDRESS), clientRS.getString(EMAIL), clientRS.getString(PHONE_NUMBER),
                                        clientRS.getString(PASSWORD)));
                    }
                }
            } return loanableItems;
        };
    }
}
