package com.soen343.project.service.catalog;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.service.database.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.soen343.project.database.connection.DatabaseConnector.executeQuery;
import static com.soen343.project.database.connection.DatabaseConnector.executeQueryExpectMultiple;
import static com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation.findAllLoanables;
import static com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation.queryLoanableAndItemByItemspecType;
import static com.soen343.project.repository.entity.EntityConstants.ID;

@Service
public class Catalog {

    private final static String LATEST_ITEM_ID_QUERY = "SELECT id FROM LoanableItem ORDER BY id DESC LIMIT 1;";

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
        catalogSessions.put(sessionID, new CatalogSession(scheduler, executeQuery(LATEST_ITEM_ID_QUERY, (rs, statement) -> rs.getLong(ID))));
        return sessionID;
    }

    public void addCatalogItem(String sessionID, ItemSpecification itemSpec) {
        Item itemToAdd = library.createItem(itemSpec);
        CatalogSession session = getSession(sessionID);
        session.addEntry(itemToAdd);
    }

    public void deleteCatalogItem(String sessionID, ItemSpecification itemSpecification) {
        CatalogSession session = getSession(sessionID);
        session.removeItem(itemSpecification);
    }

    public ItemSpecification addItemSpec(String sessionID, ItemSpecification itemSpec) {
        CatalogSession session = getSession(sessionID);
        return session.addEntry(itemSpec);
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<?> deleteItemSpec(String sessionID, ItemSpecification itemSpec) {
        List<LoanableItem> loanedItems =
                executeQueryExpectMultiple(queryLoanableAndItemByItemspecType(itemSpec) + " and LoanableItem.available = 0;",
                        findAllLoanables());
        if (loanedItems.isEmpty()) {
            CatalogSession session = getSession(sessionID);
            session.removeEntry(itemSpec);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
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
