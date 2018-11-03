package com.soen343.project.repository.dao.catalog.item;

import com.google.common.collect.LinkedHashMultimap;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class ItemRepository implements Repository<Item> {

    @Override
    public void save(Item entity) {
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    @Override
    public void saveAll(Item... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (Item entity : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(QueryBuilder.createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue())));
        }
        uow.commit();
    }

    @Override
    public void delete(Item entity) {
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
    }

    @Override
    public Item findById(Long id) {
        return (Item) executeForeignKeyTableQuery(createFindByIdQuery(ITEM_TABLE, id), (rs, statement) -> {
            rs.next(); // Move to query result
            Long itemSpecId = rs.getLong(ITEMSPECID);
            String itemSpecType = rs.getString(TYPE);
            Long itemId = rs.getLong(ID);

            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            rs.next(); // Move to query result
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);
            return new Item(itemId, itemSpecification);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> findAll() {
        return (List<Item>) executeQueryExpectMultiple(createFindAllQuery(ITEM_TABLE), (rs, statement) -> {
            List<DatabaseEntity> list = new ArrayList<>();
            LinkedHashMultimap<Long, Item> itemTempInfo = LinkedHashMultimap.create();

            while (rs.next()) {
                Item item = new Item(rs.getLong(ID), rs.getString(TYPE));
                itemTempInfo.put(rs.getLong(ITEMSPECID), item);
            }

            for (Long itemSpecId : itemTempInfo.keySet()) {
                Set<Item> items = itemTempInfo.get(itemSpecId);
                for (Item item : items) {
                    ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(item.getType(), itemSpecId));
                    item.setSpec(getItemSpec(item.getType(), itemSpecRS, statement, itemSpecId));
                    list.add(item);
                }
            }
            return list;
        });
    }

    @Override
    public void update(Item entity) {
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }

    private ItemSpecification getItemSpec(String type, ResultSet rs, Statement statement, Long id) throws SQLException {
        if (type.equalsIgnoreCase(Music.class.getSimpleName())) {
            return Music.buildMusic(rs);
        }
        else if (type.equalsIgnoreCase(Magazine.class.getSimpleName())) {
            return Magazine.buildMagazine(rs);
        }
        else if (type.equalsIgnoreCase(Book.class.getSimpleName())) {
            return Book.buildBook(rs);
        }
        else if (type.equalsIgnoreCase(Movie.class.getSimpleName())) {
            Movie movie = Movie.buildMovie(rs, null, null, null);
            movie.setProducers(findAllFromForeignKey(statement, PRODUCERS_TABLE, id));
            movie.setActors(findAllFromForeignKey(statement, ACTORS_TABLE, id));
            movie.setDubbed(findAllFromForeignKey(statement, DUBBED_TABLE, id));
            return movie;
        }
        return null;
    }

}
