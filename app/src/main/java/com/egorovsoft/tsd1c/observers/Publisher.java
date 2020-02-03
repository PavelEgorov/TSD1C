package com.egorovsoft.tsd1c.observers;

import android.util.Log;

import com.egorovsoft.tsd1c.data.ScanItem;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private final static String TAG = "Publisher";

    private static Publisher instance = null;
    private static Object syncObj = new Object();

    private List<Observer> observers;

    private Publisher() {
        observers = new ArrayList<>();
    }

    public static Publisher getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new Publisher();
            }

            return instance;
        }
    }

    public void subscribe(Observer observer) {
        Log.d(TAG, "subscribe: " + observer.toString());
        synchronized (syncObj) {
            observers.add(observer);
        }
    }

    public void unsubscribe(Observer observer) {
        Log.d(TAG, "unsubscribe: " + observer.toString());
        synchronized (syncObj) {
            observers.remove(observer);
        }
    }

    public void notifyUpdateList(ArrayList<ScanItem> list) {
        for (Observer observer : observers) {
            Log.d(TAG, "notifyUpdateList: " + observer.toString());
            observer.updateList(list);
        }
    }
}
