package com.soen343.project.repository.entity.user;

import com.soen343.project.repository.entity.user.types.UserType;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Created by Kevin Tan 2018-09-23
 */

@NoArgsConstructor
public class Client extends User {

    @Builder
    public Client(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber) {
        super(id, firstName, lastName, physicalAddress, email, phoneNumber);
        this.userType = UserType.ADMIN;
    }
}
