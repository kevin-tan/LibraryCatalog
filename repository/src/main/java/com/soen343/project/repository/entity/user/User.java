package com.soen343.project.repository.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Kevin Tan 2018-09-23
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class User {

    private Long id;

    private String firstName;
    private String lastName;
    private String physAddress;
    private String emailAddress;
    private String phoneNumber;

}
