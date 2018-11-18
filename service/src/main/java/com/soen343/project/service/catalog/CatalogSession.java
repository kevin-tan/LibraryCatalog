package com.soen343.project.service.catalog;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.uow.UnitOfWork;
import lombok.Data;

import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.MAGAZINE_TABLE;

@Data
class CatalogSession {

    private final String id;
    private UnitOfWork unitOfWork;
    private final Scheduler scheduler;

    CatalogSession(String sessionID, Scheduler scheduler) {
        this.id = sessionID;
        this.scheduler = scheduler;
        this.unitOfWork = new UnitOfWork();
    }

    void updateEntry(ItemSpecification itemSpec) {
        unitOfWork.registerOperation(
                statement -> statement.executeUpdate(createUpdateQuery(itemSpec.getTable(), itemSpec.sqlUpdateValues(), itemSpec.getId())));
    }

    void addEntry(Item item) {
        if (item.getTable().equals(MAGAZINE_TABLE)) {
            unitOfWork.registerOperation(
                    statement -> statement.executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue())));
        } else {
            unitOfWork.registerOperation(LoanableItemOperation.saveQuery((LoanableItem) item));
        }
    }

    void addEntry(ItemSpecification itemSpec) {
        unitOfWork.registerOperation(
                statement -> statement.executeUpdate(createSaveQuery(itemSpec.getTableWithColumns(), itemSpec.toSQLValue())));
    }

    void removeEntry(Item item) {
        unitOfWork.registerOperation(statement -> statement.executeUpdate(createDeleteQuery(item.getTable(), item.getId())));
    }

    void removeEntry(ItemSpecification itemSpec) {
        unitOfWork.registerOperation(statement -> statement.executeUpdate(createDeleteQuery(itemSpec.getTable(), itemSpec.getId())));
    }

    void endSession() {
        scheduler.writer_p();
        unitOfWork.commit();
        scheduler.writer_v();
    }
}
