package com.soen343.project.service.catalog;

import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CatalogSearch {

    private static final String BOOK = "Book";
    private static final String MAGAZINE = "Magazine";
    private static final String MOVIE = "Movie";
    private static final String MUSIC = "Music";

    private final MovieGateway movieRepository;
    private final BookGateway bookRepository;
    private final MagazineGateway magazineRepository;
    private final MusicGateway musicRepository;

    @Autowired
    public CatalogSearch(MovieGateway movieRepository, BookGateway bookRepository, MagazineGateway magazineRepository, MusicGateway musicRepository) {
        this.movieRepository = movieRepository;
        this.bookRepository = bookRepository;
        this.magazineRepository = magazineRepository;
        this.musicRepository = musicRepository;
    }


    public List<Object> searchCatalogByAttribute(String itemType, String attribute, String attributeValue) {
        List<Object> itemspecs = new LinkedList<>();

        if(itemType.equalsIgnoreCase(BOOK)) {
            itemspecs.add(bookRepository.findByAttribute(attribute, attributeValue));
        } else if (itemType.equalsIgnoreCase(MAGAZINE)) {
            itemspecs.add(magazineRepository.findByAttribute(attribute, attributeValue));
        } else if (itemType.equalsIgnoreCase(MOVIE)) {
            itemspecs.add(movieRepository.findByAttribute(attribute, attributeValue));
        } else if (itemType.equalsIgnoreCase(MUSIC)) {
            itemspecs.add(musicRepository.findByAttribute(attribute, attributeValue));
        }

        return itemspecs;
    }
}
