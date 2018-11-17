package com.soen343.project.repository.entity.transaction;

import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LoanDays {
    public static final Map<String, Integer> DAYS_UNTIL_DUE;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(Book.class.getSimpleName(), Book.DAYS_UNTIL_DUE);
        map.put(Movie.class.getSimpleName(), Movie.DAYS_UNTIL_DUE);
        map.put(Music.class.getSimpleName(), Music.DAYS_UNTIL_DUE);
        DAYS_UNTIL_DUE = Collections.unmodifiableMap(map);
    }
}
