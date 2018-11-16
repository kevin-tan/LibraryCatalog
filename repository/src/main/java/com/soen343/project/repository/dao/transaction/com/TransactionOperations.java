package com.soen343.project.repository.dao.transaction.com;

import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.soen343.project.database.query.QueryBuilder.createFindByIdQuery;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.getItemSpec;
import static com.soen343.project.repository.entity.EntityConstants.*;

/**
 * Created by Kevin Tan 2018-11-16
 */

public final class TransactionOperations {

    private TransactionOperations() {}

    public static void createTransactions(List<Transaction> transactions, Statement statement) throws SQLException {
        for (Transaction transaction : transactions) {
            // LoanableItem
            ResultSet loanableRS = statement.executeQuery(createFindByIdQuery(LOANABLEITEM_TABLE, transaction.getLoanableItem().getId()));
            while (loanableRS.next()) {
                transaction.getLoanableItem().setAvailable(Boolean.valueOf(loanableRS.getString(AVAILABLE)));
            }

            // Item + ItemSpecification
            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, transaction.getLoanableItem().getId()));
            String itemType = null;
            Long itemSpecId = null;
            while (itemRS.next()) {
                itemType = itemRS.getString(TYPE);
                itemSpecId = itemRS.getLong(ITEMSPECID);
            }
            ResultSet itemSpecRs = statement.executeQuery(createFindByIdQuery(itemType, itemSpecId));
            transaction.getLoanableItem().setSpec(getItemSpec(itemType, itemSpecRs, statement, itemSpecId));

            // Client
            ResultSet clientRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, transaction.getClient().getId()));
            while (clientRS.next()) {
                Client client = new Client(clientRS.getLong(ID), clientRS.getString(FIRST_NAME), clientRS.getString(LAST_NAME),
                        clientRS.getString(PHYSICAL_ADDRESS), clientRS.getString(EMAIL), clientRS.getString(PHONE_NUMBER),
                        clientRS.getString(PASSWORD));
                transaction.setClient(client);
                transaction.getLoanableItem().setClient(client);
            }
        }
    }
}
