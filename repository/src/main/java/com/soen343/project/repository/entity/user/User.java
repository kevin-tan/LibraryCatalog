package com.soen343.project.repository.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Kevin Tan 2018-09-23
 */

@NoArgsConstructor
@Data
public abstract class User {

    protected Long id;

    protected String firstName;
    protected String lastName;
    protected String physicalAddress;
    protected String email;
    protected String phoneNumber;
    protected String userType;

    public User(String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.physicalAddress = physicalAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String sqlColumnValues() {
        String columnValues = "firstName = " + firstName + ",";
        columnValues += "lastName = " + lastName + ",";
        columnValues += "physicalAddress = " + physicalAddress + ",";
        columnValues += "email = " + email + ",";
        columnValues += "phoneNumber = " + phoneNumber;
        return columnValues;
    }

    // toString in the format for SQL query of object type User
    public String toSQLValue() {
        return "(" + "'" + firstName + "'," + "'" + lastName + "'," + "'" + physicalAddress + "'," + "'" + email + "'," + "'" +
               phoneNumber + "'," + "'" + userType + "')";
    }
}
