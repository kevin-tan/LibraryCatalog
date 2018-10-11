package com.soen343.project.service.notification;

public interface Subject <E>{

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(E data);
}
