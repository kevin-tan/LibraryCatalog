package com.soen343.project.repository.dao.catalog.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedHashMultimap;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.getItemSpec;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class ItemGateway implements Gateway<Item> {

    private final Scheduler scheduler;

    @Autowired
    public ItemGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Item entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Item... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (Item entity : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(QueryBuilder.createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Item entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public Item findById(Long id) {
        scheduler.reader_p();
        Item item = (Item) executeForeignKeyTableQuery(createFindByIdQuery(ITEM_TABLE, id), (rs, statement) -> {
            rs.next(); // Move to query result
            Long itemSpecId = rs.getLong(ITEMSPECID);
            String itemSpecType = rs.getString(TYPE);
            Long itemId = rs.getLong(ID);

            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            rs.next(); // Move to query result
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);
            return new Item(itemId, itemSpecification);
        });
        scheduler.reader_v();
        return item;
    }

    @Override
    public List<Item> findByAttribute(Map<String, String> attributeValue) {
        //Not required at the moment
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> findAll() {
        scheduler.reader_p();
        List<Item> list = (List<Item>) executeQueryExpectMultiple(createFindAllQuery(ITEM_TABLE), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Item entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    public List<?> findByItemSpecId(String itemType, Long itemSpecId) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(
                createSearchByAttributesQuery(ITEM_TABLE, ImmutableMap.of(TYPE, itemType, ITEMSPECID, itemSpecId.toString())),
                databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    public List<?> findByItemSpec(String itemType) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(ITEM_TABLE, TYPE, itemType), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    private DatabaseQueryOperation databaseQueryOperation() {
        return (rs, statement) -> {
            List<DatabaseEntity> listTemp = new ArrayList<>();
            Map<String, LinkedHashMultimap<Long, Item>> itemTempInfo = new HashMap<>();

            while (rs.next()) {
                String itemType = rs.getString(TYPE);
                Item item = new Item(rs.getLong(ID), itemType);
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
                        listTemp.add(item);
                    }
                }
            }
            return listTemp;
        };
    }
}
