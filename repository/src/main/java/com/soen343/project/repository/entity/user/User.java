package com.soen343.project.repository.entity.user;

import com.soen343.project.repository.entity.DatabaseEntity;
import lombok.NoArgsConstructor;

import static com.soen343.project.repository.entity.DatabaseTables.USER_TABLE;

/**
 * Created by Kevin Tan 2018-09-23
 */

@NoArgsConstructor
public abstract class User implements DatabaseEntity {

    protected Long id;

    private String firstName;
    private String lastName;
    private String physicalAddress;
    private String email;
    private String phoneNumber;
    String userType;

    User(String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.physicalAddress = physicalAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getTable() {
        return USER_TABLE;
    }

    @Override
    public String sqlColumnValues() {
        String columnValues = "firstName = " + firstName + ",";
        columnValues += "lastName = " + lastName + ",";
        columnValues += "physicalAddress = " + physicalAddress + ",";
        columnValues += "email = " + email + ",";
        columnValues += "phoneNumber = " + phoneNumber;
        return columnValues;
    }

    @Override
    public String toSQLValue() {
        return "(" + "'" + firstName + "'," + "'" + lastName + "'," + "'" + physicalAddress + "'," + "'" + email + "'," + "'" +
               phoneNumber + "'," + "'" + userType + "')";
    }

    @Override
    public Long getId() {
        return id;
    }
}
