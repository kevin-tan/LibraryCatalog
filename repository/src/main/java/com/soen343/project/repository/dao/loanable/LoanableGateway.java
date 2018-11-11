package com.soen343.project.repository.dao.loanable;

import com.google.common.collect.LinkedHashMultimap;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.loanable.Loan;
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

@Component
public class LoanableGateway implements Gateway<Loan> {

    private final Scheduler scheduler;

    @Autowired
    public LoanableGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Loan entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Loan... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (Loan loan : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(createSaveQuery(loan.getTableWithColumns(), loan.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Loan entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();

    }

    @Override
    public Loan findById(Long id) {

        scheduler.reader_p();
        Loan loan = (Loan) executeForeignKeyTableQuery(createFindByIdQuery(LOAN_TABLE, id), (rs, statement) -> {
            rs.next();// Move to query result
            Long loanId = rs.getLong(ID);
            Long itemId = rs.getLong(ITEMID);
            Date checkoutDate = convertToDate(rs.getString(CHECKOUTDATE));
            Date dueDate = convertToDate(rs.getString(DUEDATE));

            rs.next();
            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, itemId));
            itemRS.next();
            Long itemSpecId = itemRS.getLong(ITEMSPECID);
            String itemSpecType = itemRS.getString(TYPE);

            itemRS.next();
            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

            Item item = new Item(itemId, itemSpecification);

            return new Loan(loanId, item, checkoutDate, dueDate);

        });
        scheduler.reader_v();
        return loan;
    }

    @Override
    public List<Loan> findByAttribute(Map<String, String> attributeValue) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Loan> findAll() {
        scheduler.reader_p();
        List<Loan> list = (List<Loan>) executeQueryExpectMultiple(createFindAllQuery(LOAN_TABLE, ITEM_TABLE, ID, ITEMID),(rs,statement)->{
            List<DatabaseEntity> listTemp = new ArrayList<>();
            Map<String, LinkedHashMultimap<Long,Item>> itemTempInfo = new HashMap<>();
//           Map<Long, Item> loanTempInfo = new HashMap<>();
            List<Loan> loans = new ArrayList<>();

            while (rs.next()) {
                String itemType = rs.getString(TYPE);
                Item item = new Item(rs.getLong(ITEMID), itemType);
                Loan loan = new Loan(rs.getLong(ID), item, convertToDate(rs.getString(CHECKOUTDATE)), convertToDate(rs.getString(DUEDATE)));
                loans.add(loan);
                if (itemTempInfo.get(itemType) == null) {
                    itemTempInfo.put(itemType, LinkedHashMultimap.create());
                }
                itemTempInfo.get(itemType).put(rs.getLong(ITEMSPECID), item);
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

            return loans;
        });
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Loan entity) {
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
