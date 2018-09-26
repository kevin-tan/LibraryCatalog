package com.soen343.project.service.database;

import com.soen343.project.repository.dao.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Kevin Tan 2018-09-25
 */
@Service
public class RecordDatabase {

    private final UserRepository userRepository;

    @Autowired
    public RecordDatabase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Define methods to access repositories
}
