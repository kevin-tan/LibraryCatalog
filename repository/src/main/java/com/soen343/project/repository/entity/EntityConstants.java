package com.soen343.project.repository.entity;

public final class EntityConstants {

    private EntityConstants() {}

    public static final String ID = "id";

    //Tables
    public static final String USER_TABLE = "User";
    public static final String USER_TABLE_WITH_COLUMNS =
            USER_TABLE + "(firstName, lastName, physicalAddress, email, phoneNumber, userType, password)";
    public static final String ITEMSPEC_TABLE = "ItemSpecification";
    public static final String ITEMSPEC_TABLE_WITH_COLUMNS =
            ITEMSPEC_TABLE + "(itemspecificationid)";
    public static final String MAGAZINE_TABLE = "Magazine";
    public static final String MAGAZINE_TABLE_WITH_COLUMNS =
            MAGAZINE_TABLE + "(title, publisher, pubDate, language, isbn10, isbn13)";
    public static final String BOOK_TABLE = "Book";
    public static final String BOOK_TABLE_WITH_COLUMNS =
            BOOK_TABLE + "(title, publisher, pubDate, language, isbn10, isbn13, author, format, pages)";
    public static final String MOVIE_TABLE = "ItemSpecification";
    public static final String MOVIE_TABLE_WITH_COLUMNS =
            MOVIE_TABLE + "(title, releaseDate, director, language, subtitles, runTime)";
    public static final String MUSIC_TABLE = "ItemSpecification";
    public static final String MUSIC_TABLE_WITH_COLUMNS =
            MUSIC_TABLE + "(title, releaseDate, type, artist, label, asin)";

    //User
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHYSICAL_ADDRESS = "physicalAddress";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String USER_TYPE = "userType";
    public static final String USERNAME= "username";
    public static final String PASSWORD = "password";

    //Items
    public static final String ITEMSPECID = "itemspecificationid";
    public static final String TITLE = "title";
    public static final String RELEASEDATE = "releaseDate";
    public static final String PUBLISHER = "publisher";
    public static final String PUBDATE = "pubDate";
    public static final String LANGUAGE = "language";
    public static final String ISBN10 = "isbn10";
    public static final String ISBN13 = "isbn13";
    public static final String AUTHOR = "author";
    public static final String FORMAT = "format";
    public static final String PAGES = "pages";
    public static final String DIRECTOR = "director";
    public static final String SUBTITLES = "subtitles";
    public static final String RUNTIME = "runTime";
    public static final String TYPE = "type";
    public static final String ARTIST = "artist";
    public static final String LABEL = "label";
    public static final String ASIN = "asin";
}