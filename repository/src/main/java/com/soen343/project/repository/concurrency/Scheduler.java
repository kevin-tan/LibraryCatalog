package com.soen343.project.repository.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

/**
 * Created by Kevin Tan 2018-11-02
 */

@Component
public class Scheduler {

    private Semaphore writers;
    private Semaphore readers;
    private Integer numberOfWriters;

    public Scheduler() {
        numberOfWriters = Integer.valueOf(0);
        readers = new Semaphore(1);
        writers = new Semaphore(1);
    }

    public void addWriter() {
        try {
            writers.acquire();
            synchronized (numberOfWriters) {
                numberOfWriters++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeWriter() {
        writers.release();
        synchronized (numberOfWriters) {
            numberOfWriters--;
        }
    }

    public void addReader(){

    }

    public void remodeReader(){

    }
}
