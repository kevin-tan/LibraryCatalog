package com.soen343.project.repository.dao.transaction;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.transaction.com.TransactionGateway;
import com.soen343.project.repository.dao.transaction.com.TransactionOperations;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.transaction.ReturnTransaction;
import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.getItemSpec;
import static com.soen343.project.repository.dao.transaction.com.DateConverter.convertToDate;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class ReturnTransactionGateway implements TransactionGateway<ReturnTransaction> {

    private final Scheduler scheduler;

    @Autowired
    public ReturnTransactionGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(ReturnTransaction entity) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> modifyTransaction(statement, entity));
        scheduler.writer_v();
    }

    private void modifyTransaction(Statement statement, ReturnTransaction transaction) throws SQLException {
        transaction.getLoanableItem().setAvailable(true);
        transaction.getLoanableItem().setClient(null);
        statement.executeUpdate(createUpdateQuery(transaction.getLoanableItem().getTable(), transaction.getLoanableItem().sqlUpdateValues(),
                transaction.getLoanableItem().getId()));
        statement.executeUpdate(createSaveQuery(transaction.getTableWithColumns(), transaction.toSQLValue()));
    }

    @Override
    public void saveAll(ReturnTransaction... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (ReturnTransaction transaction : entities) {
            uow.registerOperation(statement -> modifyTransaction(statement, transaction));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(ReturnTransaction entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public ReturnTransaction findById(Long id) {
        scheduler.reader_p();
        ReturnTransaction loanTransaction =
                (ReturnTransaction) executeForeignKeyTableQuery(createFindByIdQuery(RETURNTRANSACTION_TABLE, id), (rs, statement) -> {
                    rs.next();// Move to query result
                    Long transactionId = rs.getLong(ID);
                    Long itemId = rs.getLong(ITEMID);
                    Long userId = rs.getLong(USERID);
                    LocalDateTime transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));

                    ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, itemId));
                    itemRS.next();
                    Long itemSpecId = itemRS.getLong(ITEMSPECID);
                    String itemSpecType = itemRS.getString(TYPE);

                    ResultSet loanableItemRS = statement.executeQuery(createFindByIdQuery(LOANABLEITEM_TABLE, itemId));
                    loanableItemRS.next();
                    Boolean available = Boolean.valueOf(loanableItemRS.getString(AVAILABLE));

                    ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
                    ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

                    ResultSet userRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, userId));
                    userRS.next();
                    Client client = new Client(userId, userRS.getString(FIRST_NAME), userRS.getString(LAST_NAME),
                            userRS.getString(PHYSICAL_ADDRESS), userRS.getString(EMAIL), userRS.getString(PHONE_NUMBER),
                            userRS.getString(PASSWORD));

                    LoanableItem loanableItem = new LoanableItem(itemId, itemSpecification, available, client);
                    return new ReturnTransaction(transactionId, loanableItem, client, transactionDate);
                });
        scheduler.reader_v();
        return loanTransaction;
    }

    @Override
    public List<ReturnTransaction> findByAttribute(Map<String, String> attributeValue) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ReturnTransaction> findAll() {
        scheduler.reader_p();
        List<ReturnTransaction> list =
                (List<ReturnTransaction>) executeQueryExpectMultiple(createFindAllQuery(RETURNTRANSACTION_TABLE), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(ReturnTransaction entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public List<?> findByUserId(Long userId) {
        scheduler.reader_p();
        List<?> list =
                executeQueryExpectMultiple(createSearchByAttributeQuery(RETURNTRANSACTION_TABLE, USERID, userId), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<?> findByTransactionDate(String transactionDate) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(RETURNTRANSACTION_TABLE, TRANSACTIONDATE, transactionDate),
                findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<?> findByItemType(String itemType) {
        scheduler.reader_p();
        List transactions = executeQueryExpectMultiple(createDoubleTableQuery(RETURNTRANSACTION_TABLE, ITEM_TABLE, ITEMID, TYPE, itemType),
                findAllTransaction());
        scheduler.reader_v();
        return transactions;
    }

    private DatabaseQueryOperation findAllTransaction() {
        return (rs, statement) -> {
            List<Transaction> returnTransactions = new ArrayList<>();
            while (rs.next()) {
                returnTransactions.add(new ReturnTransaction(rs.getLong(ID), new LoanableItem(rs.getLong(ITEMID), null, null, null),
                        new Client(rs.getLong(USERID)), convertToDate(rs.getString(TRANSACTIONDATE))));
            }
            TransactionOperations.createTransactions(returnTransactions, statement);
            return returnTransactions;
        };
    }
}

