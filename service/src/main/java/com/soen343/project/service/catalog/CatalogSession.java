package com.soen343.project.service.catalog;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.uow.UnitOfWork;
import lombok.Data;

@Data
class CatalogSession {

    private final String id;
    private UnitOfWork<DatabaseEntity> unitOfWork;


    CatalogSession(String sessionID) {
        this.id = sessionID;
        this.unitOfWork = new UnitOfWork<>();
    }

    void updateEntry(ItemSpecification itemSpec) {
        unitOfWork.registerUpdate(itemSpec);
    }

    void addEntry(Item item) {
        unitOfWork.registerCreate(item);
    }

    void removeEntry(Item item) {
        unitOfWork.registerDelete(item);
    }

    void endSession() {
        unitOfWork.commit();
    }
}
