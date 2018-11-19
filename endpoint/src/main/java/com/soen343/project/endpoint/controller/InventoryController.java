package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.service.catalog.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class InventoryController {
    private final Catalog catalog;

    @Autowired
    public InventoryController(Catalog catalog) {
        this.catalog = catalog;
    }

    @PostMapping("/catalog/edit")
    public ResponseEntity<?> editCatalog() {
        return new ResponseEntity<>(catalog.createNewSession(), HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/add")
    public ResponseEntity<?> addItem(@PathVariable String sessionID, @RequestBody ItemSpecification itemSpec) {
        catalog.addCatalogItem(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/delete/{itemID}")
    public ResponseEntity<?> deleteItem(@PathVariable String sessionID, @PathVariable Long itemID) {
        catalog.deleteCatalogItem(sessionID, itemID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/addSpec")
    public ResponseEntity<?> addItemSpecification(@PathVariable String sessionID, @RequestBody ItemSpecification itemSpec) {
        catalog.addItemSpec(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/deleteSpec")
    public ResponseEntity<?> deleteItemSpecification(@PathVariable String sessionID, @RequestBody ItemSpecification itemSpec) {
        return catalog.deleteItemSpec(sessionID, itemSpec);
    }

    @PostMapping("/catalog/{sessionID}/modifySpec")
    public ResponseEntity<?> modifyItemSpecification(@PathVariable String sessionID, @RequestBody ItemSpecification itemSpec) {
        catalog.modifyItemSpec(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/save")
    public ResponseEntity<?> save(@PathVariable String sessionID) {
        return new ResponseEntity<>(catalog.endSession(sessionID), HttpStatus.OK);
    }
}