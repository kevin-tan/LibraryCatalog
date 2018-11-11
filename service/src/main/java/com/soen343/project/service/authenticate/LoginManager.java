package com.soen343.project.service.authenticate;

import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.database.Library;
import com.soen343.project.service.notification.Observer;
import com.soen343.project.service.notification.Subject;
import com.soen343.project.service.registry.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class LoginManager implements Subject<User> {

    private Library library;
    private List<Observer> observers;

    @Autowired
    LoginManager(UserRegistry userRegistry, Library library) {
        this.library = library;
        observers = new LinkedList<>();
        addObserver(userRegistry);
    }

    public boolean loginUser(String email){
        User user = library.getUserByEmail(email);
        if (user != null){
            notifyObservers(user, true);
            return true;
        }
        return false;
    }

    public boolean logoutUser(String email) {
        User user = library.getUserByEmail(email);
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
