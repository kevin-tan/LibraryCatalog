package com.soen343.project.repository.dao.catalog.item;

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
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation.*;
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
        executeBatchUpdate(saveQuery(entity));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(LoanableItem... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (LoanableItem loanableItem : entities) {
            uow.registerOperation(saveQuery(loanableItem));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(LoanableItem entity) {
        scheduler.writer_p();
        executeBatchUpdate(deleteQuery(entity));
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
                (List<LoanableItem>) executeQueryExpectMultiple(createFindAllQuery(LOANABLEITEM_TABLE), findAllLoanables());
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

    public List<?> findByItemSpecId(String itemType, Long itemSpecId) {
        scheduler.reader_p();
        List list = executeQueryExpectMultiple(queryLoanableAndItemByItemspecType(itemType, itemSpecId) + ";", findAllLoanables());
        scheduler.reader_v();
        return list;
    }

}

