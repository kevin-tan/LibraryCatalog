package com.soen343.project.repository.dao.catalog.item;

import com.google.common.collect.ImmutableMap;
import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.user.Client;
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
public class LoanableItemGateway implements Gateway<LoanableItem> {

    private final Scheduler scheduler;

    @Autowired
    public LoanableItemGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(LoanableItem entity) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> {
            Item item = new Item(entity.getSpec());
            statement.executeQuery(createSaveQuery(item.getTableWithColumns(), item.toSQLValue()));
            ResultSet rs = statement.executeQuery("SELECT id FROM Item ORDER BY id DESC LIMIT 1");
            if (rs.next()) entity.setId(rs.getLong(ID));
            statement.execute(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        });
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
        executeBatchUpdate(statement -> {
            statement.execute(createDeleteQuery(item.getTable(), item.getId()));
            statement.execute(createDeleteQuery(entity.getTable(), entity.getId()));
        });
        scheduler.writer_v();
    }

    @Override
    public LoanableItem findById(Long id) {
        scheduler.reader_p();
        LoanableItem loanableItem =
                (LoanableItem) executeForeignKeyTableQuery(createFindByIdQuery(LOANABLEITEM_TABLE, id), (rs, statement) -> {
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
                    if (userRS.next()) {
                        client = new Client(userId, userRS.getString(FIRST_NAME), userRS.getString(LAST_NAME),
                                userRS.getString(PHYSICAL_ADDRESS), userRS.getString(EMAIL), userRS.getString(PHONE_NUMBER),
                                userRS.getString(PASSWORD));
                    }

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
        List<LoanableItem> list =
                (List<LoanableItem>) executeQueryExpectMultiple(createFindAllQuery(LOANABLEITEM_TABLE), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(LoanableItem entity) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> {
            Item item = new Item(entity.getSpec());
            statement.execute(createUpdateQuery(item.getTable(), item.sqlUpdateValues(), item.getId()));
            statement.execute(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        });
        scheduler.writer_v();
    }

    public List<?> findByUserIdAndIsLoaned(Long userId) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributesQuery(LOANABLEITEM_TABLE,
                ImmutableMap.of(USERID, userId.toString(), AVAILABLE, "0")), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<LoanableItem> findLoanablesAreAvailable(List<LoanableItem> loanableItems) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple((statement) -> {
            List<LoanableItem> tempList = new LinkedList<>();
            for (LoanableItem loanableItem : loanableItems) {
                ResultSet rs = statement.executeQuery(
                        "SELECT LoanableItem.* FROM LoanableItem, Item WHERE Item.itemSpecId = " + loanableItem.getSpec().getId() +
                        " and Item.type = '" + loanableItem.getType() +
                        "' and LoanableItem.available = 1 and LoanableItem.id = Item.id LIMIT 1;");
                if (!rs.next()) tempList.add(loanableItem);
            }
            return tempList;
        });
        scheduler.reader_v();
        return list;
    }

    public List<?> findItemBySpecId(Long specID) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(createSearchByAttributeQuery(LOANABLEITEM_TABLE, ITEMSPECID, specID), findAllTransaction());
        scheduler.reader_v();
        return list;
    }

    private DatabaseQueryOperation findAllTransaction() {
        return (rs, statement) -> {
            List<LoanableItem> loanableItems = new ArrayList<>();

            while (rs.next()) {
                Client client = (rs.getLong(USERID) == 0) ? null : new Client(rs.getLong(USERID));
                loanableItems.add(new LoanableItem(rs.getLong(ID), null, Boolean.valueOf(rs.getString(AVAILABLE)), client));
            }

            for (LoanableItem loanableItem : loanableItems) {
                Long itemSpecId = null;
                String itemSpecType = null;

                ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, loanableItem.getId()));
                while (itemRS.next()) {
                    itemSpecId = itemRS.getLong(ITEMSPECID);
                    itemSpecType = itemRS.getString(TYPE);
                }

                ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
                while (itemSpecRS.next()) {
                    loanableItem.setSpec(getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId));
                }

                if (loanableItem.getClient() != null) {
                    ResultSet clientRS = statement.executeQuery(createFindByIdQuery(USER_TABLE, loanableItem.getClient().getId()));
                    while (clientRS.next()) {
                        loanableItem.setClient(
                                new Client(clientRS.getLong(ID), clientRS.getString(FIRST_NAME), clientRS.getString(LAST_NAME),
                                        clientRS.getString(PHYSICAL_ADDRESS), clientRS.getString(EMAIL), clientRS.getString(PHONE_NUMBER),
                                        clientRS.getString(PASSWORD)));

                    }
                }
            }
            return loanableItems;
        };
    }
}

