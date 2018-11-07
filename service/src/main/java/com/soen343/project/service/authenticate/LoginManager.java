package com.soen343.project.service.authenticate;

import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.database.RecordDatabase;
import com.soen343.project.service.notification.Observer;
import com.soen343.project.service.notification.Subject;
import com.soen343.project.service.registry.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class LoginManager implements Subject<User> {

    private RecordDatabase recordDatabase;
    private List<Observer> observers;

    @Autowired
    LoginManager(UserRegistry userRegistry, RecordDatabase recordDatabase) {
        this.recordDatabase = recordDatabase;
        observers = new LinkedList<>();
        addObserver(userRegistry);
    }

    public boolean loginUser(String email){
        User user = recordDatabase.getUserByEmail(email);
        if (user != null){
            notifyObservers(user, true);
            return true;
        }
        return false;
    }

    public boolean logoutUser(String email) {
        User user = recordDatabase.getUserByEmail(email);
        if (user != null){
            notifyObservers(user, false);
            return true;
        }
        return false;
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(User data, boolean islogin) {
        for (Observer observer: observers) {
            observer.update(data, islogin);
        }
    }
}
