package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.service.catalog.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PostMapping("/catalog/add")
    public ResponseEntity<?> addItem(@RequestBody ItemSpecification itemSpec, @RequestBody String sessionID){
        return new ResponseEntity<>(catalog.addCatalogItem(sessionID, itemSpec), HttpStatus.OK);
    }

    @PostMapping("/catalog/delete/{itemID}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemID, @RequestBody String sessionID){
        return new ResponseEntity<>(catalog.deleteCatalogItem(sessionID, itemID), HttpStatus.OK);
    }

    @PostMapping("/catalog/save")
    public ResponseEntity<?> save(@RequestBody String sessionID){
        return new ResponseEntity<>(catalog.endSession(sessionID), HttpStatus.OK);
    }

    /*
    @PostMapping("/catalog/modify/{itemID}")
    public ResponseEntity<?> modifyItem(@PathVariable Long itemID, @RequestBody ItemSpecification itemSpec){
        return new ResponseEntity<>(catalog.editItem(itemID, itemSpec), HttpStatus.OK);
    }

    @PostMapping("/catalog")
    public ResponseEntity<?> viewCatalogInventory(){
        return new ResponseEntity<>(catalog.getAllItem(), HttpStatus.OK);
    }*/
}
