package com.soen343.project.repository.entity.user;

import com.soen343.project.repository.entity.user.types.UserType;
import lombok.NoArgsConstructor;

/**
 * Created by Kevin Tan 2018-09-23
 */

@NoArgsConstructor
public class Client extends User {

    public Client(String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        super(firstName, lastName, physicalAddress, email, phoneNumber);
        this.userType = UserType.ADMIN;
    }
}
