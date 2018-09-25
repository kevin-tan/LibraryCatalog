package com.soen343.project.repository.entity.user;

import com.soen343.project.database.base.DatabaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.soen343.project.repository.EntityConstants.*;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Data
@NoArgsConstructor
public abstract class User implements DatabaseEntity {

    private Long id;

    private String firstName;
    private String lastName;
    private String physicalAddress;
    private String email;
    private String phoneNumber;
    String userType;


    User(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.physicalAddress = physicalAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = FIRST_NAME + " = '" + firstName + "', ";
        columnValues += LAST_NAME + " = '" + lastName + "', ";
        columnValues += PHYSICAL_ADDRESS + " = '" + physicalAddress + "', ";
        columnValues += EMAIL + " = '" + email + "', ";
        columnValues += PHONE_NUMBER + " = '" + phoneNumber + "'";
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
