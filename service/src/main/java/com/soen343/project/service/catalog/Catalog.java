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

    public void modifyCatalogItem(String sessionID, ItemSpecification itemSpec) {
        CatalogSession session = getSession(sessionID);
        session.updateEntry(itemSpec);
    }

    public void addCatalogItem(String sessionID, ItemSpecification itemSpec) {
        Item itemToAdd = recordDatabase.createItem(itemSpec);
        CatalogSession session = getSession(sessionID);
        session.addEntry(itemToAdd);
    }

    public void deleteCatalogItem(String sessionID, Long itemID) {
        Item itemToDelete = recordDatabase.getItem(itemID);
        CatalogSession session = getSession(sessionID);
        session.removeEntry(itemToDelete);
    }

    public List<Item> endSession(String sessionID) {
        CatalogSession session = getSession(sessionID);
        session.endSession();
        catalogSessions.remove(session);
        return getAllItems();
    }

    public List<Item> getAllItems(){
        return recordDatabase.findAllItems();
    }

    private CatalogSession getSession(String sessionID) {
        return catalogSessions.stream()
                .filter(s -> s.getId().equals(sessionID))
                .findAny()
                .orElse(null);
    }

}
