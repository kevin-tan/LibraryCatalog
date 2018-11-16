package com.soen343.project.repository.dao.transaction;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
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
            uow.registerOperation(
                    statement -> executeUpdate(createSaveQuery(transaction.getTableWithColumns(), transaction.toSQLValue())));
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
        LoanTransaction loanTransaction = (LoanTransaction) executeForeignKeyTableQuery(createFindByIdQuery(LOANTRANSACTION_TABLE, id), (rs, statement) -> {
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
            Boolean available = loanableItemRS.getBoolean(AVAILABLE);

            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

            ResultSet userRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, userId));
            userRS.next();
            Client client = new Client(userId, userRS.getString(FIRST_NAME), userRS.getString(LAST_NAME), userRS.getString(PHYSICAL_ADDRESS),
                    userRS.getString(EMAIL), userRS.getString(PHONE_NUMBER), userRS.getString(PASSWORD));


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
        List<LoanTransaction> list = (List<LoanTransaction>) executeQueryExpectMultiple(createFindAllQuery(LOANTRANSACTION_TABLE), (rs, statement) -> {
            List<LoanTransaction> loanTransactions = new ArrayList<>();

            while (rs.next()) {
                Long transactionId = rs.getLong(ID);
                Long itemId = rs.getLong(ITEMID);
                Long userId = rs.getLong(USERID);
                Date transactionDate = convertToDate(rs.getString(TRANSACTIONDATE));
                Date dueDate = convertToDate(rs.getString(DUEDATE));

                final Long[] itemSpecId = {null};
                final String[] itemSpecType = {null};
                executeQuery(createFindByIdQuery(ITEM_TABLE, itemId), (rs1, statement1) -> {
                    rs1.next();
                    itemSpecId[0] = rs1.getLong(ITEMSPECID);
                    itemSpecType[0] = rs1.getString(TYPE);
                    return null;
                });

                final Boolean[] available = {null};
                executeQuery(createFindByIdQuery(LOANABLEITEM_TABLE, itemId), (rs2, statement2) -> {
                    rs2.next();
                    available[0] = rs2.getBoolean(AVAILABLE);
                    return null;
                });

                ItemSpecification itemSpecification = (ItemSpecification) executeQuery(createFindByIdQuery(itemSpecType[0], itemSpecId[0]), (rs3, statement3) -> {
                    return getItemSpec(itemSpecType[0], rs3, statement3, itemSpecId[0]);
                });

                final Client[] client = {null};
                executeQuery(createFindByIdQuery(USER_TABLE, userId), (rs4, statement4) -> {
                    rs4.next();
                    client[0] = new Client(userId, rs4.getString(FIRST_NAME), rs4.getString(LAST_NAME), rs4.getString(PHYSICAL_ADDRESS),
                            rs4.getString(EMAIL), rs4.getString(PHONE_NUMBER), rs4.getString(PASSWORD));
                    return null;
                });

                LoanableItem loanableItem = new LoanableItem(itemId, itemSpecification, available[0], client[0]);
                LoanTransaction loanTransaction = new LoanTransaction(transactionId, loanableItem, client[0], transactionDate, dueDate);
                loanTransactions.add(loanTransaction);
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
