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

    public List<User> register(User regUser){
        List<User> users = findAllUsers();
        //Checks if user's email, address, and phone is unique)
        for(User iUser: users) {
            if (iUser.getEmail().equals(regUser.getEmail()) || (iUser.getPhoneNumber().equals(regUser.getPhoneNumber()) || iUser.getPhysicalAddress().equals(regUser.getPhysicalAddress()))) {
                return null;
            }
        }
        userRepository.save(regUser);
        return findAllUsers();
    }
}
