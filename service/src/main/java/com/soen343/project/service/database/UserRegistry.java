package com.soen343.project.service.database;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.ActiveUser;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.entity.user.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRegistry {

    private final UserRepository userRepository;
    private List<ActiveUser> activeUsers;

    @Autowired
    public UserRegistry(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.activeUsers = new ArrayList<>();
    }

    //TODO:This gets all users. We need login feature to view active users.
    public List<User> viewActiveUserRegistry(Long id) {
        if (authenticateAdmin(id)){
            return userRepository.findAll();
        }
        return null;
    }

    private boolean authenticateAdmin(Long id) {
        User user = userRepository.findById(id);
        if (user == null || !user.getUserType().equals(UserType.ADMIN)) {
            return false;
        } else {
            return true;
        }
    }

    private void addActiveUser(User user) {
        ActiveUser activeUser = new ActiveUser(user.getId());
        activeUsers.add(activeUser);
    }
}
