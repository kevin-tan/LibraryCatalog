package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.service.catalog.Catalog;
import com.soen343.project.service.catalog.CatalogSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CatalogController {
    private final Catalog catalog;

    private final CatalogSearch catalogSearch;

    @Autowired
    public CatalogController(Catalog catalog, CatalogSearch catalogSearch){
        this.catalog = catalog;
        this.catalogSearch = catalogSearch;
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

    @GetMapping("/catalog/findByTitle/{titleValue}")
    public ResponseEntity<?> searchCatalogByTitle(@PathVariable String titleValue) {
        return new ResponseEntity<>(catalogSearch.searchAllByTitle(titleValue), HttpStatus.OK);
    }

    @GetMapping("/catalog/search/{itemType}")
    public ResponseEntity<?> searchCatalogByAttribute(@PathVariable String itemType, @RequestBody ObjectNode objectNode) {
        Map<String, String> attributeValue = new HashMap<>();
        objectNode.fieldNames().forEachRemaining(key ->
                attributeValue.put(key,objectNode.get(key).asText())
        );

        return new ResponseEntity<>(catalogSearch.searchCatalogByAttribute(itemType, attributeValue), HttpStatus.OK);
    }

}
