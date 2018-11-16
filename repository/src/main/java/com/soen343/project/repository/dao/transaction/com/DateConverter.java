package com.soen343.project.repository.dao.transaction.com;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Kevin Tan 2018-11-16
 */

public final class DateConverter {

    private DateConverter() {}

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime convertToDate(String sqlTimestamp) {
        return LocalDateTime.parse(sqlTimestamp, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

}
