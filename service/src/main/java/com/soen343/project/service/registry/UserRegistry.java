package com.soen343.project.service.registry;

import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.instance.ActiveUser;
import com.soen343.project.service.database.RecordDatabase;
import com.soen343.project.service.notification.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserRegistry implements Observer<User> {

    private final RecordDatabase recordDatabase;
    List<ActiveUser> activeUsers;

    @Autowired
    public UserRegistry(RecordDatabase recordDatabase) {
        this.recordDatabase = recordDatabase;
        this.activeUsers = new LinkedList<>();
    }

    public List<ActiveUser> viewActiveUserRegistry() {
        return activeUsers;
    }

    private void addActiveUser(User user) {
        for (ActiveUser activeUser : activeUsers) {
            if (activeUser.getId().equals(user.getId())) {
                return;
            }
        }
        ActiveUser activeUser = new ActiveUser(user.getId());
        activeUsers.add(activeUser);
    }

    private void removeActiveUser(User user) {
        for (ActiveUser activeUser : activeUsers) {
            if (activeUser.getId().equals(user.getId())) {
                activeUsers.remove(activeUser);
                return;
            }
        }
    }

    private boolean isActiveUser(User user) {
        for (ActiveUser activeUser : activeUsers) {
            if (activeUser.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(User data, boolean isLogin) {
        if (isLogin && !isActiveUser(data)) {
            addActiveUser(data);
        } else if (!isLogin && isActiveUser(data)){
            removeActiveUser(data);
        }
    }
}
