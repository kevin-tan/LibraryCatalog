package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
@RequestMapping("/user")
public class CatalogController {
    private final Catalog catalog;

    private final CatalogSearch catalogSearch;

    @Autowired
    public CatalogController(Catalog catalog, CatalogSearch catalogSearch) {
        this.catalog = catalog;
        this.catalogSearch = catalogSearch;
    }

    @GetMapping("/catalog")
    public ResponseEntity<?> viewCatalogInventory() {
        return new ResponseEntity<>(catalog.getAllItems(), HttpStatus.OK);
    }

    @GetMapping("/catalog/findByTitle/{titleValue}")
    public ResponseEntity<?> searchCatalogByTitle(@PathVariable String titleValue) {
        return new ResponseEntity<>(catalogSearch.searchAllByTitle(titleValue), HttpStatus.OK);
    }

    @PostMapping("/catalog/search/{itemType}")
    public ResponseEntity<?> searchCatalogByAttribute(@PathVariable String itemType, @RequestBody ObjectNode objectNode) {
        Map<String, String> attributeValue = new HashMap<>();
        objectNode.fieldNames().forEachRemaining(key -> attributeValue.put(key, objectNode.get(key).asText()));

        return new ResponseEntity<>(catalogSearch.searchCatalogByAttribute(itemType, attributeValue), HttpStatus.OK);
    }

    @GetMapping("/catalog/searchAll")
    public ResponseEntity<?> getAllCatalog() {
        return new ResponseEntity<>(catalogSearch.getAllItemSpecs(), HttpStatus.OK);
    }

    @GetMapping("/catalog/getAll/{itemType}")
    public ResponseEntity<?> getAllOfType(@PathVariable String itemType) {
        return new ResponseEntity<>(catalogSearch.getAllOfType(itemType), HttpStatus.OK);
    }

    @GetMapping("/catalog/getAll/{itemType}/{itemSpecId}")
    public ResponseEntity<?> getAllOfSameType(@PathVariable String itemType, @PathVariable Long itemSpecId) {
        return new ResponseEntity<>(catalogSearch.getAllOfSameType(itemType, itemSpecId), HttpStatus.OK);
    }
}
