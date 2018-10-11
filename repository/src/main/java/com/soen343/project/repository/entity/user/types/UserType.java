package com.soen343.project.repository.entity.user.types;

import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;

/**
 * Created by Kevin Tan 2018-09-24
 */
public final class UserType {

    private UserType() {}

    public static String ADMIN = Admin.class.getSimpleName();
    public static String CLIENT = Client.class.getSimpleName();

}
