package com.soen343.project.database.base;

/**
 * Created by Kevin Tan 2018-09-24
 */

public interface DatabaseEntity {

    String sqlUpdateValues();

    String toSQLValue();

    Long getId();
}
