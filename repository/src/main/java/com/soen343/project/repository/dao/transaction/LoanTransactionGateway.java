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
public class LoanTransactionGateway implements TransactionGateway<LoanTransaction> {

    private final Scheduler scheduler;

    @Autowired
    public LoanTransactionGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(LoanTransaction entity) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> {
            ResultSet rs = statement.executeQuery(saveQuery(entity));
            modifyLoanableItem(rs, statement, entity);
        });
        scheduler.writer_v();
    }

    private String saveQuery(LoanTransaction transaction) {
        return "SELECT LoanableItem.* FROM LoanableItem, Item WHERE Item.itemSpecId = " + transaction.getLoanableItem().getSpec().getId() +
               " and Item.type  = '" + transaction.getLoanableItem().getType() +
               "' and LoanableItem.id = Item.id and LoanableItem.available = 1 LIMIT 1;";
    }

    private void modifyLoanableItem(ResultSet rs, Statement statement, LoanTransaction transaction) throws SQLException {
        if (rs.next()) {
            transaction.setId(rs.getLong(ID));
            transaction.getLoanableItem().setAvailable(false);
            transaction.getLoanableItem().setClient(transaction.getClient());
            statement.executeUpdate(
                    createUpdateQuery(transaction.getLoanableItem().getTable(), transaction.getLoanableItem().sqlUpdateValues(),
                            transaction.getLoanableItem().getId()));
            statement.executeUpdate(createSaveQuery(transaction.getTableWithColumns(), transaction.toSQLValue()));
        }
    }

    @Override
    public void saveAll(LoanTransaction... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (LoanTransaction transaction : entities) {
            uow.registerOperation(statement -> {
                ResultSet rs = statement.executeQuery(saveQuery(transaction));
                modifyLoanableItem(rs, statement, transaction);
            });
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
        LoanTransaction loanTransaction = executeForeignKeyTableQuery(createFindByIdQuery(LOANTRANSACTION_TABLE, id), (rs, statement) -> {
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
        List<LoanTransaction> list = executeQueryExpectMultiple(createFindAllQuery(LOANTRANSACTION_TABLE), findAllTransaction());
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
    public List<LoanTransaction> findByTransactionDate(String transactionDate) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, TRANSACTIONDATE, transactionDate),
                findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<LoanTransaction> findByUserId(Long userId) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, USERID, userId), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<LoanTransaction> findByItemType(String itemType) {
        scheduler.reader_p();
        List loanTransactions = executeQueryExpectMultiple(createDoubleTableQuery(LOANTRANSACTION_TABLE, ITEM_TABLE, ITEMID, TYPE, itemType),
                        findAllTransaction());
        scheduler.reader_v();
        return loanTransactions;
    }

    public List<LoanTransaction> findByDueDate(String dueDate) {
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
