package com.soen343.project.service.catalog;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.service.database.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Catalog {

    private final Library library;
    private final Scheduler scheduler;
    private Map<String, CatalogSession> catalogSessions;

    @Autowired
    public Catalog(Library library, Scheduler scheduler) {
        this.library = library;
        this.scheduler = scheduler;
        this.catalogSessions = new HashMap<>();
    }

    public String createNewSession() {
        String sessionID = UUID.randomUUID().toString();
        catalogSessions.put(sessionID, new CatalogSession(sessionID, scheduler));
        return sessionID;
    }

    public void addCatalogItem(String sessionID, ItemSpecification itemSpec) {
        Item itemToAdd = library.createItem(itemSpec);
        CatalogSession session = getSession(sessionID);
        session.addEntry(itemToAdd);
    }

    public void deleteCatalogItem(String sessionID, Long itemID) {
        Item itemToDelete = library.getItem(itemID);
        CatalogSession session = getSession(sessionID);
        session.removeEntry(itemToDelete);
    }

    public void addItemSpec(String sessionID, ItemSpecification itemSpec) {
        CatalogSession session = getSession(sessionID);
        session.addEntry(itemSpec);
    }

    public void deleteItemSpec(String sessionID, ItemSpecification itemSpec) {
        CatalogSession session = getSession(sessionID);
        session.removeEntry(itemSpec);
    }

    public void modifyItemSpec(String sessionID, ItemSpecification itemSpec) {
        CatalogSession session = getSession(sessionID);
        session.updateEntry(itemSpec);
    }

    public List<Item> endSession(String sessionID) {
        CatalogSession session = getSession(sessionID);
        session.endSession();
        catalogSessions.remove(sessionID);
        return getAllItems();
    }

    public List<Item> getAllItems() {
        return library.findAllItems();
    }

    private CatalogSession getSession(String sessionID) {
        return catalogSessions.get(sessionID);
    }
}
