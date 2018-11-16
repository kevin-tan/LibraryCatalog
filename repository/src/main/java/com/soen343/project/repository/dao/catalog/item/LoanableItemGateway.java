package com.soen343.project.repository.dao.catalog.item;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
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
        Item item = new Item(entity.getSpec());
        executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue()));
        executeQuery("SELECT id FROM item ORDER BY id DESC LIMIT 1", ((rs, statement) -> {
            rs.next();
            entity.setId(rs.getLong(ID));
            return null;
        }));
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
        Item item = new Item(entity.getSpec());
        executeUpdate(createDeleteQuery(item.getTable(), item.getId()));
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
            Boolean available = Boolean.valueOf(rs.getString(AVAILABLE));

            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, loanableItemId));
            itemRS.next();
            Long itemSpecId = itemRS.getLong(ITEMSPECID);
            String itemSpecType = itemRS.getString(TYPE);

            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

            ResultSet userRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, userId));
            Client client = null;
            if (userRS.next())
                client = new Client(userId, userRS.getString(FIRST_NAME), userRS.getString(LAST_NAME), userRS.getString(PHYSICAL_ADDRESS),
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
        scheduler.reader_p();
        List<LoanableItem> list = (List<LoanableItem>) executeQueryExpectMultiple(createFindAllQuery(LOANABLEITEM_TABLE), (rs, statement) -> {
            List<LoanableItem> loanableItems = new ArrayList<>();

            while (rs.next()) {
                Long loanableItemId = rs.getLong(ID);
                Long userId = rs.getLong(USERID);
                Boolean available = Boolean.valueOf(rs.getString(AVAILABLE));

                final Long[] itemSpecId = {null};
                final String[] itemSpecType = {null};
                executeQuery(createFindByIdQuery(ITEM_TABLE, loanableItemId), (rs1, statement1) -> {
                    rs1.next();
                    itemSpecId[0] = rs1.getLong(ITEMSPECID);
                    itemSpecType[0] = rs1.getString(TYPE);
                    return null;
                });

                ItemSpecification itemSpecification = (ItemSpecification) executeQuery(createFindByIdQuery(itemSpecType[0], itemSpecId[0]), (rs2, statement2) -> {
                    return getItemSpec(itemSpecType[0], rs2, statement2, itemSpecId[0]);
                });

                final Client[] client = {null};
                executeQuery(createFindByIdQuery(USER_TABLE, userId), (rs3, statement3) -> {
                    if (rs3.next())
                        client[0] = new Client(userId, rs3.getString(FIRST_NAME), rs3.getString(LAST_NAME), rs3.getString(PHYSICAL_ADDRESS),
                                rs3.getString(EMAIL), rs3.getString(PHONE_NUMBER), rs3.getString(PASSWORD));
                    else
                        client[0] = null;
                    return null;
                });

                LoanableItem loanableItem = new LoanableItem(loanableItemId, itemSpecification, available, client[0]);
                loanableItems.add(loanableItem);
            }

            return loanableItems;
        });
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(LoanableItem entity) {
        scheduler.writer_p();
        Item item = new Item(entity.getSpec());
        executeUpdate(createUpdateQuery(item.getTable(), item.sqlUpdateValues(), item.getId()));
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
}

