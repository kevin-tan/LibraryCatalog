package com.soen343.project.repository.dao.transaction;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.transaction.com.TransactionGateway;
import com.soen343.project.repository.dao.transaction.com.TransactionOperations;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
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
@SuppressWarnings("unchecked")
public class LoanTransactionGateway implements TransactionGateway<LoanTransaction> {

    private final Scheduler scheduler;

    @Autowired
    public LoanTransactionGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(LoanTransaction entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(LoanTransaction... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (LoanTransaction transaction : entities) {
            uow.registerOperation(statement -> statement.executeQuery(createSaveQuery(transaction.getTableWithColumns(), transaction.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(LoanTransaction entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public LoanTransaction findById(Long id) {
        scheduler.reader_p();
        LoanTransaction loanTransaction =
                (LoanTransaction) executeForeignKeyTableQuery(createFindByIdQuery(LOANTRANSACTION_TABLE, id), (rs, statement) -> {
                    rs.next();// Move to query result
                    Long transactionId = rs.getLong(ID);
                    Long itemId = rs.getLong(ITEMID);
                    Long userId = rs.getLong(USERID);
                    LocalDateTime transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));
                    LocalDateTime dueDate = convertToDate(rs.getString(DUEDATE));

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
                    return new LoanTransaction(transactionId, loanableItem, client, transactionDate, dueDate);
                });
        scheduler.reader_v();
        return loanTransaction;
    }

    @Override
    public List<LoanTransaction> findByAttribute(Map<String, String> attributeValue) {
        return null;
    }

    @Override
    public List<LoanTransaction> findAll() {
        scheduler.reader_p();
        List<LoanTransaction> list =
                (List<LoanTransaction>) executeQueryExpectMultiple(createFindAllQuery(LOANTRANSACTION_TABLE), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(LoanTransaction entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public List<?> findByTransactionDate(String transactionDate) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, TRANSACTIONDATE, transactionDate),
                findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<?> findByUserId(Long userId) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, USERID, userId), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<?> findByItemType(String itemType) {
        scheduler.reader_p();
        List loanTransactions =
                executeQueryExpectMultiple(createDoubleTableQuery(LOANTRANSACTION_TABLE, ITEM_TABLE, ITEMID, TYPE, itemType),
                        findAllTransaction());
        scheduler.reader_v();
        return loanTransactions;
    }

    public List<?> findByDueDate(String dueDate) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, DUEDATE, dueDate), findAllTransaction());
        scheduler.reader_v();
        return list;
    }


    private DatabaseQueryOperation findAllTransaction() {
        return (rs, statement) -> {
            List<Transaction> loanTransactions = new ArrayList<>();
            // Long id, LoanableItem loanableItem, Client client, Date transactionDate, Date dueDate
            while (rs.next()) {
                loanTransactions.add(new LoanTransaction(rs.getLong(ID), new LoanableItem(rs.getLong(ITEMID), null, null, null),
                        new Client(rs.getLong(USERID)), convertToDate(rs.getString(TRANSACTIONDATE)),
                        convertToDate(rs.getString(DUEDATE))));
            }
            TransactionOperations.createTransactions(loanTransactions, statement);
            return loanTransactions;
        };
    }

}
