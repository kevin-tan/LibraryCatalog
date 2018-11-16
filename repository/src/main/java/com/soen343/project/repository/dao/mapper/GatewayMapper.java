package com.soen343.project.repository.dao.mapper;

import com.google.common.collect.ImmutableMap;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.dao.transaction.ReturnTransactionGateway;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GatewayMapper {

    private final Map<String, Gateway> gatewayMap;

    private static final String BOOK = "book";
    private static final String MAGAZINE = "magazine";
    private static final String MOVIE = "movie";
    private static final String MUSIC = "music";
    private static final String LOAN_TRANSACTION = "loanTransaction";
    private static final String RETURN_TRANSACTION = "returnTransaction";

    @Autowired
    public GatewayMapper(MovieGateway movieRepository, BookGateway bookRepository, MagazineGateway magazineRepository,
                         MusicGateway musicRepository) {
        gatewayMap = ImmutableMap.of(BOOK, bookRepository, MAGAZINE, magazineRepository, MOVIE, movieRepository, MUSIC, musicRepository);
    }

    public GatewayMapper(LoanTransactionGateway loanTransactionGateway, ReturnTransactionGateway returnTransactionGateway){
        gatewayMap = ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway, RETURN_TRANSACTION, returnTransactionGateway);
    }

    public Gateway getGateway(String itemType) {
        return gatewayMap.get(itemType.toLowerCase());
    }
}
