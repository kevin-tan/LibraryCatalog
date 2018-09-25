package com.soen343.project.repository.entity;

/**
 * Created by Kevin Tan 2018-09-24
 */

public interface DatabaseEntity {

    String getTable();

    String sqlColumnValues();

    String toSQLValue();

    Long getId();
}
