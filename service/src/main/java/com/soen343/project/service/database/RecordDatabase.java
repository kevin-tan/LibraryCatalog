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

    //Define methods to access repositories
    //TODO:This gets all users. We need login feature to view active users.
    public List<User> viewActiveUserRegistry() {
        return userRepository.findAll();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //I am assuming that the user's ID is already given by the system
    public List<User> register(User regUser){
        List<User> users = getAllUsers();

        //Checks if user's email is used (phone, and physical address don't count because of family members)
        for(User iUser: users){
            if(iUser.getEmail().equals(regUser.getEmail())){
                return null;
            }

            if((iUser.getFirstName().equals(regUser.getFirstName()) && iUser.getLastName().equals(regUser.getLastName()))
                    && (iUser.getPhoneNumber().equals(regUser.getPhoneNumber()) || iUser.getPhysicalAddress().equals(regUser.getPhysicalAddress()))){
                return null;
            }
        }
        userRepository.save(regUser);
        return getAllUsers();
    }
}
