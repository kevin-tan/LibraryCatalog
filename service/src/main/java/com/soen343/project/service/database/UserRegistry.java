package com.soen343.project.service.database;

import com.soen343.project.repository.entity.user.ActiveUser;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.entity.user.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserRegistry {

    private final RecordDatabase recordDatabase;
    private List<ActiveUser> activeUsers;

    @Autowired
    public UserRegistry(RecordDatabase recordDatabase) {
        this.recordDatabase = recordDatabase;
        this.activeUsers = new LinkedList<>();
    }

    //TODO:This gets all users for now. We need login feature to view active users.
    public List<User> viewActiveUserRegistry(Long id) {
        if (authenticateAdmin(id)){
            return recordDatabase.findAllUsers();
        }
        return null;
    }

    private boolean authenticateAdmin(Long id) {
        User user = recordDatabase.findUserById(id);
        return user != null && user.getUserType().equals(UserType.ADMIN);
    }

    private void addActiveUser(User user) {
        ActiveUser activeUser = new ActiveUser(user.getId());
        activeUsers.add(activeUser);
    }
}
