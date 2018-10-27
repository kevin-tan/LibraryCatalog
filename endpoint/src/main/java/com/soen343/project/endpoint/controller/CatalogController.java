package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.catalog.itemspec.MovieRepository;
import com.soen343.project.repository.dao.catalog.itemspec.MusicRepository;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.catalog.Music;
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
    private final MusicRepository musicRepository;

    @Autowired
    public CatalogController(Catalog catalog, MusicRepository movieRepository){
        this.catalog = catalog;
        this.musicRepository = movieRepository;
    }


    @PostMapping("/catalog/edit")
    public ResponseEntity<?> editCatalog(){
        return new ResponseEntity<>(catalog.createNewSession(), HttpStatus.OK);
    }

    @PostMapping("/catalog/modify")
    public ResponseEntity<?> modifyItem(@RequestBody ItemSpecification itemSpec, @RequestBody String sessionID){
        catalog.modifyCatalogItem(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/add")
    public ResponseEntity<?> addItem(@RequestBody ItemSpecification itemSpec, @RequestBody String sessionID){
        catalog.addCatalogItem(sessionID, itemSpec);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/delete/{itemID}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemID, @RequestBody String sessionID){
        catalog.deleteCatalogItem(sessionID, itemID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/catalog/save")
    public ResponseEntity<?> save(@RequestBody String sessionID){
        return new ResponseEntity<>(catalog.endSession(sessionID), HttpStatus.OK);
    }

    @PostMapping("/catalog")
    public ResponseEntity<?> viewCatalogInventory(){
        return new ResponseEntity<>(catalog.getAllItems(), HttpStatus.OK);
    }
}
