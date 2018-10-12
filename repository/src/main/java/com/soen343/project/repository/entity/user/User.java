package com.soen343.project.repository.entity.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soen343.project.database.base.DatabaseEntity;
import lombok.Data;

import static com.soen343.project.repository.entity.EntityConstants.*;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({@JsonSubTypes.Type(value = Admin.class, name = "Admin"), @JsonSubTypes.Type(value = Client.class, name = "Client")})
public abstract class User implements DatabaseEntity {

    private Long id;
    private String firstName;
    private String lastName;
    private String physicalAddress;
    private String email;
    private String phoneNumber;
    private String password;

    User() {}

    User(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.physicalAddress = physicalAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password=password;
    }

    @Override
    public String sqlUpdateValues() {
        String columnValues = FIRST_NAME + " = '" + firstName + "', ";
        columnValues += LAST_NAME + " = '" + lastName + "', ";
        columnValues += PHYSICAL_ADDRESS + " = '" + physicalAddress + "', ";
        columnValues += EMAIL + " = '" + email + "', ";
        columnValues += PHONE_NUMBER + " = '" + phoneNumber + "', ";
        columnValues += PASSWORD + " = '" + password + "'";

        return columnValues;
    }

    @Override
    public String toSQLValue() {
        return "('" + firstName + "','" + lastName + "','" + physicalAddress + "','" + email + "','" +
               phoneNumber + "','" + getClass().getSimpleName() + "','" + password +"')";
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getUserType() {
        return getClass().getSimpleName();
    }
}
