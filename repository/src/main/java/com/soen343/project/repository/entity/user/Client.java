package com.soen343.project.repository.entity.user;

import lombok.Builder;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class Client extends User {

    public Client(){}

    public Client(Long id){
        super(id, "", "", "", "", "" ,"");
    }

    @Builder
    public Client(Long id, String firstName, String lastName, String physicalAddress, String email, String phoneNumber, String password) {
        super(id, firstName, lastName, physicalAddress, email, phoneNumber, password);
    }
}
