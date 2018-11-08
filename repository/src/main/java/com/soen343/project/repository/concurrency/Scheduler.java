package com.soen343.project.repository.concurrency;

import org.springframework.stereotype.Component;
import java.util.concurrent.Semaphore;

/**
 * Created by Kevin Tan/Sagar Patel 2018-11-02
 */

@Component
@SuppressWarnings("ALL")
public class Scheduler {
    // Semaphore
    private Semaphore writerBlock;
    private Semaphore readBlock;
    private Semaphore writePending;

    // Mutex
    private Semaphore mutex1;
    private Semaphore mutex2;

    // Counter
    private int numberOfWriters;
    private int numberOfReaders;

    public Scheduler() {
        numberOfWriters = 0;
        numberOfReaders = 0;

        readBlock = new Semaphore(1, true);
        writerBlock = new Semaphore(1, true);
        writePending = new Semaphore(1, true);

        mutex1 = new Semaphore(1, true);
        mutex2 = new Semaphore(1, true);
    }

    public void reader_p() {
        try {
            writePending.acquire();
            readBlock.acquire();

            mutex1.acquire();
            numberOfReaders++;
            if (numberOfReaders == 1) writerBlock.acquire();
            mutex1.release();

            readBlock.release();
            writePending.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reader_v() {
        try {
            mutex1.acquire();
            numberOfReaders--;
            if (numberOfReaders == 0) writerBlock.release();
            mutex1.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writer_p() {
        try {
            mutex2.acquire();
            numberOfWriters++;
            if (numberOfWriters == 1) readBlock.acquire();
            mutex2.release();

            writerBlock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writer_v() {
        try {
            writerBlock.release();
            mutex2.acquire();
            numberOfWriters--;
            if (numberOfWriters == 0) readBlock.release();
            mutex2.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}