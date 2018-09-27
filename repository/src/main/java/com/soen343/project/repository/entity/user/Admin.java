package com.soen343.project.repository.entity.user;

import com.soen343.project.repository.entity.user.types.UserType;
import lombok.Builder;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class Admin extends User {

    public Admin() {
        super(UserType.ADMIN);
    }

    @Builder
    public Admin(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        super(id, firstName, lastName, physicalAddress, email, phoneNumber, UserType.ADMIN);
    }
}
