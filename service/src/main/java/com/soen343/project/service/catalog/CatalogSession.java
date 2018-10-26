package com.soen343.project.service.catalog;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.uow.UnitOfWork;

class CatalogSession {

    private final String id;
    private UnitOfWork<Item> itemUnitOfWork;


    CatalogSession(String sessionID) {
        this.id = sessionID;
        this.itemUnitOfWork = new UnitOfWork<>();
    }
}
