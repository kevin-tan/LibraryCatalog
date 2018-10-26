package com.soen343.project.service.catalog;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.service.database.RecordDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Catalog {

    private final RecordDatabase recordDatabase;

    @Autowired
    public Catalog(RecordDatabase recordDatabase){
        this.recordDatabase = recordDatabase;
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

    public List<Item> getAllItem(){
        return recordDatabase.findAllItem();
    }*/
}
