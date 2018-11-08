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

    //TODO: Change to String parameter insted of User
    public boolean loginUser(User username){
        // must be called after successful login
        notifyObservers(username);
        return true;
    }

    //TODO: Change to String parameter insted of User
    public boolean logoutUser(User username) {
        // must be called after successful logout
        notifyObservers(username);
        return true;
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
    public void notifyObservers(User data) {
        for (Observer observer: observers) {
            observer.update(data);
        }
    }
}
