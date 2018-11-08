package com.soen343.project.endpoint.controller;

import com.soen343.project.service.catalog.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class CatalogController {
    private final Catalog catalog;

    @Autowired
    public CatalogController(Catalog catalog){
        this.catalog = catalog;
    }

    @GetMapping("/catalog")
    public ResponseEntity<?> viewCatalogInventory(){
        return new ResponseEntity<>(catalog.getAllItems(), HttpStatus.OK);
    }
}
