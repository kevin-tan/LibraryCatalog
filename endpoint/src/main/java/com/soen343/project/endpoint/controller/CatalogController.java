package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.service.catalog.Catalog;
import com.soen343.project.service.catalog.CatalogSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static com.soen343.project.repository.entity.EntityConstants.TITLE;

@Controller
@RequestMapping("/admin")
public class CatalogController {
    private final Catalog catalog;

    private final MovieGateway movieRepository;
    private final BookGateway bookRepository;
    private final MagazineGateway magazineRepository;
    private final MusicGateway musicRepository;
    private final CatalogSearch catalogSearch;

    @Autowired
    public CatalogController(Catalog catalog, MovieGateway movieRepository, BookGateway bookRepository, MagazineGateway magazineRepository, MusicGateway musicRepository, CatalogSearch catalogSearch){
        this.catalog = catalog;
        this.movieRepository = movieRepository;
        this.bookRepository = bookRepository;
        this.magazineRepository = magazineRepository;
        this.musicRepository = musicRepository;
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
        List<Object> itemspecs = new LinkedList<>();

        itemspecs.add(movieRepository.findByAttribute(TITLE, titleValue));
        itemspecs.add(bookRepository.findByAttribute(TITLE, titleValue));
        itemspecs.add(musicRepository.findByAttribute(TITLE, titleValue));
        itemspecs.add(magazineRepository.findByAttribute(TITLE, titleValue));

        return new ResponseEntity<>(itemspecs, HttpStatus.OK);
    }

    @GetMapping("/test/{itemType}/{attribute}/{attributeValue}")
    public ResponseEntity<?> searchCatalogByAttribute(@PathVariable String itemType, @PathVariable String attribute, @PathVariable String attributeValue) {
        return new ResponseEntity<>(catalogSearch.searchCatalogByAttribute(itemType, attribute, attributeValue), HttpStatus.OK);
    }

}
