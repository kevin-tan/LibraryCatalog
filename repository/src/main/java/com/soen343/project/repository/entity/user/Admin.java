package com.soen343.project.repository.entity.user;

import lombok.Builder;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class Admin extends User {

    public Admin(){}

    @Builder
    public Admin(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber, String username, String password) {
        super(id, firstName, lastName, physicalAddress, email, phoneNumber, username, password);
    }
}
