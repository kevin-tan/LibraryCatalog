package com.soen343.project.repository.dao.transaction;

import com.google.common.collect.LinkedHashMultimap;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.transaction.ReturnTransaction;
import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
import static com.soen343.project.repository.entity.EntityConstants.*;
import static com.soen343.project.repository.entity.transaction.types.TransactionType.LOANTRANSACTION;

@Component
public class ReturnTransactionGateway implements Gateway<Transaction> {

    private final Scheduler scheduler;

    @Autowired
    public ReturnTransactionGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Transaction entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Transaction... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (Transaction transaction : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(createSaveQuery(transaction.getTableWithColumns(), transaction.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Transaction entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public Transaction findById(Long id) {

        scheduler.reader_p();
        ReturnTransaction transaction = (ReturnTransaction) executeForeignKeyTableQuery(createFindByIdQuery(RETURNTRANSACTION_TABLE, id), (rs, statement) -> {
            rs.next();// Move to query result
            Long transactionId = rs.getLong(ID);
            Long itemId = rs.getLong(ITEMID);
            Long userId = rs.getLong(USERID);
            Date transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));

            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, itemId));
            itemRS.next();
            Long itemSpecId = itemRS.getLong(ITEMSPECID);
            String itemSpecType = itemRS.getString(TYPE);

            ResultSet LoanableItemRS = statement.executeQuery(createFindByIdQuery(LOANABLEITEM_TABLE, itemId));
            LoanableItemRS.next();
            Boolean available = itemRS.getBoolean(AVAILABLE);

            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

            ResultSet userRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, userId));
            Client client = new Client(userId, userRS.getString(FIRST_NAME), userRS.getString(LAST_NAME), userRS.getString(PHYSICAL_ADDRESS),
                    userRS.getString(EMAIL), userRS.getString(PHONE_NUMBER), userRS.getString(PASSWORD));


            LoanableItem loanableItem = new LoanableItem(itemId, itemSpecification, available, client);
            return new ReturnTransaction(transactionId, loanableItem, client, transactionDate);
        });
        scheduler.reader_v();
        return transaction;
    }

    @Override
    public List<Transaction> findByAttribute(Map<String, String> attributeValue) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> findAll() {
        scheduler.reader_p();
        List<Transaction> list = (List<Transaction>) executeQueryExpectMultiple(createFindAllQuery(RETURNTRANSACTION_TABLE), (rs, statement) -> {
            Map<String, LinkedHashMultimap<Long, Item>> itemTempInfo = new HashMap<>();
            List<Transaction> transactions = new ArrayList<>();

            while (rs.next()) {
                Long transactionId = rs.getLong(ID);
                Long userId = rs.getLong(USERID);

                ResultSet transactionRS = statement.executeQuery(createFindByIdQuery(RETURNTRANSACTION_TABLE, transactionId));
                transactionRS.next();
                Long itemId = rs.getLong(ITEMID);
                Date transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));

                ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, itemId));
                itemRS.next();
                String itemType = itemRS.getString(TYPE);

                ResultSet LoanableItemRS = statement.executeQuery(createFindByIdQuery(LOANABLEITEM_TABLE, itemId));
                LoanableItemRS.next();
                Boolean available = itemRS.getBoolean(AVAILABLE);

                ResultSet userRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, userId));
                userRS.next();
                Client client = new Client(userId,userRS.getString(FIRST_NAME),userRS.getString(LAST_NAME), userRS.getString(PHYSICAL_ADDRESS),
                        userRS.getString(EMAIL),userRS.getString(PHONE_NUMBER),userRS.getString(PASSWORD));

                LoanableItem loanableItem = new loanableItem(itemRS.getLong(ITEMID), itemType);
                Transaction ReturnTransaction = new ReturnTransaction(transactionId, loanableItem, client, transactionDate);
                transactions.add(ReturnTransaction);

                if (itemTempInfo.get(itemType) == null) {
                    itemTempInfo.put(itemType, LinkedHashMultimap.create());
                }
                itemTempInfo.get(itemType).put(rs.getLong(ITEMSPECID), loanableItem);
            }

            for (String itemType : itemTempInfo.keySet()) {
                LinkedHashMultimap<Long, Item> map = itemTempInfo.get(itemType);
                for (Long itemSpecId : map.keySet()) {
                    Set<Item> items = map.get(itemSpecId);
                    ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemType, itemSpecId));
                    ItemSpecification itemSpecification = getItemSpec(itemType, itemSpecRS, statement, itemSpecId);
                    for (Item item : items) {
                        item.setSpec(itemSpecification);
                    }
                }
            }

            return transactions;
        });
        scheduler.reader_v();
        return null;
    }

    @Override
    public void update(Transaction entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    private ItemSpecification getItemSpec(String type, ResultSet rs, Statement statement, Long id) throws SQLException {
        if (type.equalsIgnoreCase(Music.class.getSimpleName())) {
            return Music.buildMusic(rs);
        } else if (type.equalsIgnoreCase(Book.class.getSimpleName())) {
            return Book.buildBook(rs);
        } else if (type.equalsIgnoreCase(Movie.class.getSimpleName())) {
            Movie movie = Movie.buildMovie(rs, null, null, null);
            movie.setProducers(findAllFromForeignKey(statement, PRODUCERS_TABLE, id));
            movie.setActors(findAllFromForeignKey(statement, ACTORS_TABLE, id));
            movie.setDubbed(findAllFromForeignKey(statement, DUBBED_TABLE, id));
            return movie;
        }
        return null;
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
}

