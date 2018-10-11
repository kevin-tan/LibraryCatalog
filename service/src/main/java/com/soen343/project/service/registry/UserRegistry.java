package com.soen343.project.service.registry;

import com.soen343.project.repository.instance.ActiveUser;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.authenticate.AuthenticationService;
import com.soen343.project.service.database.RecordDatabase;
import com.soen343.project.service.notification.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserRegistry implements Observer {

    private final RecordDatabase recordDatabase;
    private final AuthenticationService authenticationService;

    private List<ActiveUser> activeUsers;

    @Autowired
    public UserRegistry(RecordDatabase recordDatabase, AuthenticationService authenticationService) {
        this.recordDatabase = recordDatabase;
        this.activeUsers = new LinkedList<>();
        this.authenticationService = authenticationService;
    }

    //TODO:This gets all users for now. We need login feature to view active users.
    public List<ActiveUser> viewActiveUserRegistry(Long id) {
        if (this.authenticationService.authenticateAdmin(id)) {
            return activeUsers;
        }
        return null;
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
    public void update(Object data) {
        if (isActiveUser((User) data)) {
            removeActiveUser((User) data);
        } else {
            addActiveUser((User) data);
        }
    }
}
