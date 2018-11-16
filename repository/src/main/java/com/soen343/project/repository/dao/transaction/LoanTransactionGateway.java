package com.soen343.project.repository.dao.transaction;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.getItemSpec;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class LoanTransactionGateway implements Gateway<LoanTransaction> {

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
            uow.registerOperation(statement -> executeUpdate(createSaveQuery(transaction.getTableWithColumns(), transaction.toSQLValue())));
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
                    Date transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));
                    Date dueDate = convertToDate(rs.getString(DUEDATE));

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
    @SuppressWarnings("unchecked")
    public List<LoanTransaction> findAll() {
        scheduler.reader_p();
        List<LoanTransaction> list =
                (List<LoanTransaction>) executeQueryExpectMultiple(createFindAllQuery(LOANTRANSACTION_TABLE), (rs, statement) -> {
                    List<LoanTransaction> loanTransactions = new ArrayList<>();

                    // Long id, LoanableItem loanableItem, Client client, Date transactionDate, Date dueDate
                    while (rs.next()) {
                        Long transactionId = rs.getLong(ID);
                        Long itemId = rs.getLong(ITEMID);
                        Long userId = rs.getLong(USERID);
                        Date transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));
                        Date dueDate = convertToDate(rs.getString(DUEDATE));
                        loanTransactions
                                .add(new LoanTransaction(transactionId, new LoanableItem(itemId, null, null, null), new Client(userId),
                                        transactionDate, dueDate));
                    }

                    for (LoanTransaction loanTransaction : loanTransactions) {
                        // LoanableItem
                        ResultSet loanableRS =
                                statement.executeQuery(createFindByIdQuery(LOANABLEITEM_TABLE, loanTransaction.getLoanableItem().getId()));
                        while (loanableRS.next()) {
                            loanTransaction.getLoanableItem().setAvailable(Boolean.valueOf(loanableRS.getString(AVAILABLE)));
                        }

                        // Item + ItemSpecification
                        ResultSet itemRS =
                                statement.executeQuery(createFindByIdQuery(ITEM_TABLE, loanTransaction.getLoanableItem().getId()));
                        String itemType = null;
                        while (itemRS.next()) {
                            itemType = itemRS.getString(TYPE);
                        }
                        ResultSet itemSpecRs =
                                statement.executeQuery(createFindByIdQuery(itemType, loanTransaction.getLoanableItem().getId()));
                        loanTransaction.getLoanableItem()
                                .setSpec(getItemSpec(itemType, itemSpecRs, statement, loanTransaction.getLoanableItem().getId()));

                        // Client
                        ResultSet clientRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, loanTransaction.getClient().getId()));
                        while (clientRS.next()) {
                            Client client = new Client(clientRS.getLong(ID), clientRS.getString(FIRST_NAME), clientRS.getString(LAST_NAME),
                                    clientRS.getString(PHYSICAL_ADDRESS), clientRS.getString(EMAIL), clientRS.getString(PHONE_NUMBER),
                                    clientRS.getString(PASSWORD));
                            loanTransaction.setClient(client);
                            loanTransaction.getLoanableItem().setClient(client);
                        }
                    }
                    return loanTransactions;
                });
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(LoanTransaction entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    private Date convertToDate(String sqlTimestamp) {
        SimpleDateFormat sqlTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sqlTimestampFormat.parse(sqlTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private DatabaseQueryOperation databaseQueryOperation() {
        return (rs, statement) -> {
            return findAll();
        };
    }

    @SuppressWarnings("unchecked")
    public List<LoanTransaction> findByDueDate(Date dueDate) {
        scheduler.reader_p();
        List<LoanTransaction> list =
                (List<LoanTransaction>) executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, DUEDATE, dueDate),
                        databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<LoanTransaction> findByUserId(Long userId) {
        scheduler.reader_p();
        List<LoanTransaction> list =
                (List<LoanTransaction>) executeQueryExpectMultiple(createSearchByAttributeQuery(LOANTRANSACTION_TABLE, USERID, userId),
                        databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }
}
