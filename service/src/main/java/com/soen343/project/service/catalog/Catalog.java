package com.soen343.project.service.catalog;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.service.database.RecordDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class Catalog {

    private final RecordDatabase recordDatabase;

    private List<CatalogSession> catalogSessions;

    @Autowired
    public Catalog(RecordDatabase recordDatabase){
        this.recordDatabase = recordDatabase;
        this.catalogSessions = new ArrayList<>();
    }

    public String createNewSession() {
        String sessionID = UUID.randomUUID().toString();
        catalogSessions.add(new CatalogSession(sessionID));
        return sessionID;
    }

    public List<Item> addCatalogItem(String sessionID, ItemSpecification itemSpec) {
        Item itemToAdd = recordDatabase.createItem(itemSpec);
        CatalogSession session = getSession(sessionID);
        session.addEntry(itemToAdd);
        return getAllItems();
    }

    public List<Item> deleteCatalogItem(String sessionID, Long itemID) {
        Item itemToDelete = recordDatabase.getItem(itemID);
        CatalogSession session = getSession(sessionID);
        session.removeEntry(itemToDelete);
        return getAllItems();
    }

    public boolean endSession(String sessionID) {
        CatalogSession session = getSession(sessionID);
        session.endSession();
        return catalogSessions.remove(session);
    }

    private CatalogSession getSession(String sessionID) {
        return catalogSessions.stream()
                .filter(s -> s.getId().equals(sessionID))
                .findAny()
                .orElse(null);
    }

    private List<Item> getAllItems(){
        return recordDatabase.findAllItems();
    }

/*    public List<Item> editItem(long itemID, ItemSpecification itemSpec){
        recordDatabase.updateItem(itemID, itemSpec);
        return getAllItem();
    }

    public List<Item> deleteCatalogItem(long itemID){
        recordDatabase.removeItem(itemID);
        return getAllItem();
    }

    public List<Item> addCatalogItem(ItemSpecification itemSpec){
        recordDatabase.insertCatalogItem(itemSpec);
        return getAllItem();
    }

    */
}
