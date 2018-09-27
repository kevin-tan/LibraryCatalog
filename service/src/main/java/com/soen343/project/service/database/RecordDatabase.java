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

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> registerUser(User userToRegister) {
        List<User> registeredUsers = findAllUsers();
        // Checks if user's email and phone is unique
        for (User registeredUser : registeredUsers) {
            if (userToRegister.getEmail().equals(registeredUser.getEmail()) || userToRegister.getPhoneNumber().equals(registeredUser.getPhoneNumber())) {
                return null;
            }
        }

        userRepository.save(userToRegister);
        return findAllUsers();
    }
}
