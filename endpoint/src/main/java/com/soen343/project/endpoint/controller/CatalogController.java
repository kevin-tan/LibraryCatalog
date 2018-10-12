package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.service.catalog.Catalog;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class CatalogController {
    private final Catalog catalog;

    public CatalogController(Catalog catalog){
        this.catalog = catalog;
    }

    @PostMapping("/admin/{id}/catalog/modify/{itemID}")
    public List modifyItem(@PathVariable Long id, @PathVariable Long itemID, @RequestBody ItemSpecification itemSpec){
        return catalog.editItem(itemID, itemSpec);
    }

    @PostMapping("/admin/{id}/catalog/delete/{itemID}")
    public List deleteItem(@PathVariable Long id, @PathVariable Long itemID){
        return catalog.deleteCatalogItem(itemID);
    }

    @PostMapping("/admin/{id}/catalog/add")
    public List addItem(@PathVariable Long id, @RequestBody ItemSpecification itemSpec){
        return catalog.addCatalogItem(itemSpec);
    }

    @PostMapping("/admin/{id}/catalog")
    public List viewCatalogInventory(@PathVariable Long id){
        return catalog.getAllItem();
    }
}
