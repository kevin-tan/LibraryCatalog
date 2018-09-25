package com.soen343.project.repository;

public final class EntityConstants {

    private EntityConstants() {}

    public static final String ID = "id";

    //Tables
    public static final String USER_TABLE = "User";
    public static final String USER_TABLE_WITH_COLUMNS =
            USER_TABLE + "(firstName, lastName, physicalAddress, email, phoneNumber, userType)";

    //User
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHYSICAL_ADDRESS = "physicalAddress";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String USER_TYPE = "userType";
}