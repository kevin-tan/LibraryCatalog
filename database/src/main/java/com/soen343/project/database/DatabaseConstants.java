package com.soen343.project.database;

/**
 * Created by Kevin Tan 2018-09-23
 */

public final class DatabaseConstants {

    private DatabaseConstants() {}

    public static final String SCRIPT_CREATE_DIRECTORY = "database/src/main/resources/create";
    public static final String SCRIPT_DATA_DIRECTORY = "database/src/main/resources/data";
    public static final String CREATE_DIRECTORY = "create/";
    public static final String DATA_DIRECTORY = "data/";
    public final static String DATABASE_URL = "jdbc:sqlite:data/LibraryCatalog.db";
}
