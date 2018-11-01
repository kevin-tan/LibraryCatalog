package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.entity.catalog.*;
import com.soen343.project.service.catalog.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class CatalogController {
    private final Catalog catalog;

    @Autowired
    public CatalogController(Catalog catalog){
        this.catalog = catalog;
    }


    @PostMapping("/catalog/edit")
    public ResponseEntity<?> editCatalog(){
        return new ResponseEntity<>(catalog.createNewSession(), HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/modify")
    public ResponseEntity<?> modifyItem(@RequestBody ItemSpecification itemSpec, @PathVariable String sessionID){
        catalog.modifyCatalogItem(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/add")
    public ResponseEntity<?> addItem(@RequestBody ItemSpecification itemSpec, @PathVariable String sessionID){
        catalog.addCatalogItem(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/delete/{itemID}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemID, @PathVariable String sessionID){
        catalog.deleteCatalogItem(sessionID, itemID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/{sessionID}/save")
    public ResponseEntity<?> save(@PathVariable String sessionID){
        return new ResponseEntity<>(catalog.endSession(sessionID), HttpStatus.OK);
    }

    @PostMapping("/catalog")
    public ResponseEntity<?> viewCatalogInventory(){
        return new ResponseEntity<>(catalog.getAllItems(), HttpStatus.OK);
    }
}
