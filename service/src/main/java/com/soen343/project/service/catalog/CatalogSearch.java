package com.soen343.project.service.catalog;

import com.soen343.project.service.database.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CatalogSearch {

    private final Library library;

    @Autowired
    public CatalogSearch(Library library) {
        this.library = library;
    }

    public List<?> searchCatalogByAttribute(String itemType, Map<String, String> attributeValue) {
        return library.searchCatalogByAttribute(itemType, attributeValue);
    }

    public Map<String, List<?>> searchAllItemSpecByTitle(String titleValue) {
        return library.searchAllItemSpecByTitle(titleValue);
    }

    public Map<String, List<?>> getAllItemSpecs() {
        return library.getAllItemSpecs();
    }

    public List<?> getAllItemSpecOfType(String itemType) {
        return library.getAllItemSpecOfType(itemType);
    }

    public List<?> getAllItemSpecOfSameType(String itemType, Long itemSpecId) {
        return library.getAllItemSpecOfSameType(itemType, itemSpecId);
    }

    public Map<String, ?> getAllItemSpecQuantities() {
        return library.getAllItemSpecQuantities();
    }
}
