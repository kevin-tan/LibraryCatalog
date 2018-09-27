package com.soen343.project.service.registry;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.instance.ActiveUser;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.entity.user.types.UserType;
import com.soen343.project.service.database.RecordDatabase;
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
    public List<ActiveUser> viewActiveUserRegistry(Long id) {
        if (authenticateAdmin(id)){
            recordDatabase.findAllUsers().forEach(this::addActiveUser);
            return activeUsers;
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
