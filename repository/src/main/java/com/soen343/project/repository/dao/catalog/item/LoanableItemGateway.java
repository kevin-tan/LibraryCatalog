package com.soen343.project.repository.dao.catalog.item;

import com.google.common.collect.LinkedHashMultimap;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
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

import static com.soen343.project.database.connection.DatabaseConnector.executeForeignKeyTableQuery;
import static com.soen343.project.database.connection.DatabaseConnector.executeQueryExpectMultiple;
import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class LoanableItemGateway implements Gateway<LoanableItem> {

    private final Scheduler scheduler;

    @Autowired
    public LoanableItemGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(LoanableItem entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(LoanableItem... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (LoanableItem loanableItem : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(createSaveQuery(loanableItem.getTableWithColumns(), loanableItem.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(LoanableItem entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public LoanableItem findById(Long id) {
        scheduler.reader_p();
        LoanableItem loanableItem = (LoanableItem) executeForeignKeyTableQuery(createFindByIdQuery(LOANABLEITEM_TABLE, id), (rs, statement) -> {
            rs.next();// Move to query result
            Long loanableItemId = rs.getLong(ID);
            Long userId = rs.getLong(USERID);
            Boolean available = rs.getBoolean(AVAILABLE);

            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, loanableItemId));
            itemRS.next();
            Long itemSpecId = itemRS.getLong(ITEMSPECID);
            String itemSpecType = itemRS.getString(TYPE);

            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

            ResultSet userRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, userId));
            userRS.next();
            Client client = new Client(userId, userRS.getString(FIRST_NAME), userRS.getString(LAST_NAME), userRS.getString(PHYSICAL_ADDRESS),
                    userRS.getString(EMAIL), userRS.getString(PHONE_NUMBER), userRS.getString(PASSWORD));

            return new LoanableItem(loanableItemId, itemSpecification, available, client);
        });
        scheduler.reader_v();
        return loanableItem;
    }

    @Override
    public List<LoanableItem> findByAttribute(Map<String, String> attributeValue) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LoanableItem> findAll() {
//        scheduler.reader_p();
//        List<LoanableItem> list = (List<LoanableItem>) executeQueryExpectMultiple(createFindAllQuery(LOANABLEITEM_TABLE, ITEM_TABLE, ID, ID),(rs, statement)->{
//            Map<String, LinkedHashMultimap<Long, Item>> itemTempInfo = new HashMap<>();
//            List<LoanableItem> loanableItems = new ArrayList<>();
//
//            while (rs.next()) {
//                String itemType = rs.getString(TYPE);
//
//                Item item = new Item(rs.getLong(ITEMID), itemType);
//                LoanableItem loanableItem = new LoanableItem(rs.getLong(ID), item, convertToDate(rs.getString(CHECKOUTDATE)), convertToDate(rs.getString(DUEDATE)));
//                loanableItems.add(loanableItem);
//                if (itemTempInfo.get(itemType) == null) {
//                    itemTempInfo.put(itemType, LinkedHashMultimap.create());
//                }
//                itemTempInfo.get(itemType).put(rs.getLong(ITEMSPECID), item);
//            }
//
//            for (String itemType : itemTempInfo.keySet()) {
//                LinkedHashMultimap<Long, Item> map = itemTempInfo.get(itemType);
//                for (Long itemSpecId : map.keySet()) {
//                    Set<Item> items = map.get(itemSpecId);
//                    ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemType, itemSpecId));
//                    ItemSpecification itemSpecification = getItemSpec(itemType, itemSpecRS, statement, itemSpecId);
//                    for (Item item : items) {
//                        item.setSpec(itemSpecification);
//                    }
//                }
//            }
//
//            return loanableItems;
//        });
//        scheduler.reader_v();
        return null;
    }

    @Override
    public void update(LoanableItem entity) {
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

