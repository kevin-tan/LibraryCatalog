package com.soen343.project.service.catalog;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;
import com.soen343.project.repository.uow.UnitOfWork;
import lombok.Data;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation.queryLoanableAndItemByItemspecType;
import static com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation.saveQuery;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
class CatalogSession {

    private final String id;
    private UnitOfWork unitOfWork;
    private final Scheduler scheduler;
    private final AtomicLong specId;
    private final Map<Long, ItemSpecification> map;

    CatalogSession(String sessionID, Scheduler scheduler) {
        this.id = sessionID;
        this.scheduler = scheduler;
        this.unitOfWork = new UnitOfWork();
        this.specId = new AtomicLong();
        this.map = new HashMap<>();
    }

    void updateEntry(ItemSpecification itemSpec) {
        if (itemSpec.getTable().equals(MOVIE_TABLE)) {
            unitOfWork.registerOperation(ItemSpecificationOperation.movieUpdateOperation((Movie) itemSpec));
        } else {
            unitOfWork.registerOperation(statement -> statement
                    .executeUpdate(createUpdateQuery(itemSpec.getTable(), itemSpec.sqlUpdateValues(), itemSpec.getId())));
        }
    }

    void addEntry(Item item) {

        // If item.getSpec().getId() == null then we must search for that item spec id and then do the create saveQuery

        if (item.getSpec().getTable().equals(MAGAZINE_TABLE)) {
            unitOfWork.registerOperation(
                    statement -> statement.executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue())));
        } else {
            unitOfWork.registerOperation(saveQuery((LoanableItem) item));
        }
    }

    void addEntry(ItemSpecification itemSpec) {
        if (itemSpec.getTable().equals(MOVIE_TABLE)) {
            unitOfWork.registerOperation(ItemSpecificationOperation.movieSaveOperation((Movie) itemSpec));
        } else {
            unitOfWork.registerOperation(
                    statement -> statement.executeUpdate(createSaveQuery(itemSpec.getTableWithColumns(), itemSpec.toSQLValue())));
        }
    }

    void removeItem(ItemSpecification itemSpec) {
        if (itemSpec.getTable().equals(MAGAZINE_TABLE)) {
            unitOfWork.registerOperation(statement -> {
                ResultSet rs = statement.executeQuery(
                        "SELECT * FROM " + ITEM_TABLE + " WHERE " + TYPE + " = '" + itemSpec.getTable() + "' and " + ITEMSPECID + " = " +
                        itemSpec.getId() + " LIMIT 1;");
                rs.next(); // Move to query result
                statement.executeUpdate(createDeleteQuery(ITEM_TABLE, rs.getLong(ID)));
            });
        } else {
            unitOfWork.registerOperation(statement -> {
                ResultSet rs = statement.executeQuery(queryLoanableAndItemByItemspecType(itemSpec.getTable(), itemSpec.getId()) +
                                                      " and LoanableItem.available = 1 LIMIT 1;");
                rs.next();
                Long id = rs.getLong(ID);
                statement.execute(createDeleteQuery(ITEM_TABLE, id));
                statement.execute(createDeleteQuery(LOANABLEITEM_TABLE, id));
            });
        }
    }

    void removeEntry(ItemSpecification itemSpec) {
        if (!itemSpec.getTable().equals(MOVIE_TABLE)) {
            if (itemSpec.getTable().equals(MAGAZINE_TABLE)) {
                unitOfWork.registerOperation(deleteMagazine((Magazine) itemSpec));
            } else {
                unitOfWork.registerOperation(deleteItemSpecOperation(itemSpec));
            }
        } else {
            unitOfWork.registerOperation(movieDeleteOperation((Movie) itemSpec));
        }
    }

    void endSession() {
        scheduler.writer_p();
        unitOfWork.commit();
        scheduler.writer_v();
    }
}
