package com.soen343.project.service.database;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    public List<User> register(User userToRegister) {
        List<User> registeredUsers = userRepository.findAll();

        if(userToRegister.isUniqueFrom(registeredUsers)) {
            userRepository.save(userToRegister);
            return userRepository.findAll();
        } else {
            return null;
        }

    }
}
