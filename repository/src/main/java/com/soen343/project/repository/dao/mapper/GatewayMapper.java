package com.soen343.project.repository.dao.mapper;

import com.google.common.collect.ImmutableMap;
import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import com.soen343.project.repository.dao.catalog.itemspec.com.ItemSpecificationGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GatewayMapper {

    private final Map<String, ItemSpecificationGateway> gatewayMap;

    private static final String BOOK = "book";
    private static final String MAGAZINE = "magazine";
    private static final String MOVIE = "movie";
    private static final String MUSIC = "music";

    @Autowired
    public GatewayMapper(MovieGateway movieRepository, BookGateway bookRepository, MagazineGateway magazineRepository,
                         MusicGateway musicRepository) {
        gatewayMap = ImmutableMap.of(BOOK, bookRepository, MAGAZINE, magazineRepository, MOVIE, movieRepository, MUSIC, musicRepository);
    }
    
    public ItemSpecificationGateway getGateway(String itemType) {

        return gatewayMap.get(itemType.toLowerCase());
    }
}
