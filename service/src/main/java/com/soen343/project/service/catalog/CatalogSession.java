package com.soen343.project.service.catalog;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.uow.UnitOfWork;
import lombok.Data;

@Data
class CatalogSession {

    private final String id;
    private UnitOfWork<Item> itemUnitOfWork;


    CatalogSession(String sessionID) {
        this.id = sessionID;
        this.itemUnitOfWork = new UnitOfWork<>();
    }

    void endSession() {
        itemUnitOfWork.commit();
    }

    void removeEntry(Item item) {
        itemUnitOfWork.registerDelete(item);
    }
}
