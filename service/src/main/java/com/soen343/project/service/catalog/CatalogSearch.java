package com.soen343.project.service.catalog;

import com.google.common.collect.ImmutableMap;
import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.catalog.item.ItemGateway;
import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import com.soen343.project.repository.dao.mapper.GatewayMapper;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.soen343.project.repository.entity.EntityConstants.MAGAZINE_TABLE;

@Service
public class CatalogSearch {

    private final GatewayMapper gatewayMapper;
    private final MovieGateway movieRepository;
    private final BookGateway bookRepository;
    private final MagazineGateway magazineRepository;
    private final MusicGateway musicRepository;
    private final ItemGateway itemGateway;
    private final LoanableItemGateway loanableItemGateway;

    private static final String BOOK = "book";
    private static final String MAGAZINE = "magazine";
    private static final String MOVIE = "movie";
    private static final String MUSIC = "music";

    @Autowired
    public CatalogSearch(GatewayMapper gatewayMapper, MovieGateway movieRepository, BookGateway bookRepository,
                         MagazineGateway magazineRepository, MusicGateway musicRepository, ItemGateway itemGateway,
                         LoanableItemGateway loanableItemGateway) {
        this.gatewayMapper = gatewayMapper;
        this.movieRepository = movieRepository;
        this.bookRepository = bookRepository;
        this.magazineRepository = magazineRepository;
        this.musicRepository = musicRepository;
        this.itemGateway = itemGateway;
        this.loanableItemGateway = loanableItemGateway;
    }


    public List<?> searchCatalogByAttribute(String itemType, Map<String, String> attributeValue) {
        return gatewayMapper.getGateway(itemType).findByAttribute(attributeValue);
    }

    public Map<String, List<?>> searchAllByTitle(String titleValue) {
        return ImmutableMap.of(MOVIE, movieRepository.findByTitle(titleValue), BOOK, bookRepository.findByTitle(titleValue), MUSIC,
                musicRepository.findByTitle(titleValue), MAGAZINE, magazineRepository.findByTitle(titleValue));
    }

    public Map<String, List<?>> getAllItemSpecs() {
        return ImmutableMap.of(MOVIE, movieRepository.findAll(), BOOK, bookRepository.findAll(), MUSIC, musicRepository.findAll(), MAGAZINE,
                magazineRepository.findAll());
    }

    public List<?> getAllOfType(String itemType) {
        return gatewayMapper.getGateway(itemType).findAll();
    }

    public List<?> getAllOfSameType(String itemType, Long itemSpecId) {
        if (itemType.equals(MAGAZINE_TABLE)) {
            return itemGateway.findByItemSpecId(itemType, itemSpecId);
        }else{
            return loanableItemGateway.findByItemSpecId(itemType, itemSpecId);
        }
    }
}
