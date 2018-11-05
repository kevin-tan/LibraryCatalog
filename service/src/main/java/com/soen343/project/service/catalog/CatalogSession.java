package com.soen343.project.service.catalog;

import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.uow.UnitOfWork;
import lombok.Data;

import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;

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
        unitOfWork.registerOperation(statement -> executeUpdate(
                QueryBuilder.createUpdateQuery(itemSpec.getTable(), itemSpec.sqlUpdateValues(), itemSpec.getId())));
    }

    void addEntry(Item item) {
        unitOfWork
                .registerOperation(statement -> executeUpdate(QueryBuilder.createSaveQuery(item.getTableWithColumns(), item.toSQLValue())));
    }

    void removeEntry(Item item) {
        unitOfWork.registerOperation(statement -> executeUpdate(QueryBuilder.createDeleteQuery(item.getTable(), item.getId())));
    }

    void endSession() {
        scheduler.writer_p();
        unitOfWork.commit();
        scheduler.writer_v();
    }
}
