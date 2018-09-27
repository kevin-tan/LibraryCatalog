package com.soen343.project.repository.entity.user;

import com.soen343.project.repository.entity.user.types.UserType;
import lombok.Builder;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class Client extends User {

    public Client() {
        super(UserType.CLIENT);
    }

    @Builder
    public Client(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        super(id, firstName, lastName, physicalAddress, email, phoneNumber, UserType.CLIENT);
    }
}
