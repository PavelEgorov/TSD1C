package com.egorovsoft.gp;

import android.content.Context;
import android.os.Handler;

import com.egorovsoft.gp.data.Logic;
import com.egorovsoft.gp.interfaces.ImageManager;
import com.egorovsoft.gp.observers.Publisher;

import java.util.ArrayList;

public class MainPresenter {
    private static MainPresenter instance;
    private static Object sync = new Object();
    private int count;
    private Logic logic;

    private Injector injector;
    private boolean internetPermission;
    private boolean needSearch;

    private Handler handler;
    private Thread threadSearch;
    private Thread threadRecent;

    private ArrayList<ImageManager> imageManagers;

    private MainPresenter(){
        injector = new Injector();
        injector.inject();

        internetPermission = false;

        logic = new Logic();

        handler = new Handler();

        imageManagers = new ArrayList<>();
        needSearch = false;
    }

    public static MainPresenter getInstance(){
        synchronized (sync) {
            if (instance == null) {
                instance = new MainPresenter();
            }

            return instance;
        }
    }

    public void loadCount(Context context){
        injector.getSettingsManager().createSettings(context);
        setCount(injector.getSettingsManager().getCount());
    }

    public void saveCount(){
        injector.getSettingsManager().saveCount(this.getCount());
    }

    public void setCount(int c){
        synchronized (sync) {
            this.count = c;
        }
    }

    public int getCount(){
        return this.count;
    }

    public boolean showMessage(){
        return logic.needShowmessage();
    }

    public boolean isInternetPermission() {
        return internetPermission;
    }

    public void setInternetPermission(boolean internetPermission) {
        synchronized (sync) {
            this.internetPermission = internetPermission;
        }
    }

    public void loadRecentPhoto() {
        if(imageManagers.size() == 0) return;
        if (!internetPermission) return;

        dropThread(threadRecent);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                injector.getDataManager().getRecentPhotos();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Publisher.getInstance().notifyImageList(imageManagers);
                    }
                });
            }
        };
        threadRecent = new Thread(run);
        threadRecent.setDaemon(true);
        threadRecent.start();
    }

    public void findPhoto(String toString) {
        if (!isNeedSearch()) return;
        if (!internetPermission) return;

        dropThread(threadSearch);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                injector.getDataManager().searchPhotos(toString);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Publisher.getInstance().notifyImageList(imageManagers);
                    }
                });
            }
        };
        threadSearch = new Thread(run);
        threadSearch.setDaemon(true);
        threadSearch.start();
    }

    private void dropThread(Thread tr) {
        if (tr == null) return;

        Thread dropThread = tr;
        tr = null;
        dropThread.interrupt();
    }

    public void setImageManagers(ArrayList<ImageManager> imageManagers) {
        synchronized (sync) {
            this.imageManagers.clear();
            this.imageManagers.addAll(imageManagers);
        }
    }

    public ArrayList<ImageManager> getImageManagers(){
        return this.imageManagers;
    }

    public boolean isNeedSearch() {
        return needSearch;
    }

    public void setNeedSearch(boolean needSearch) {
        synchronized (sync) {
            this.needSearch = needSearch;
        }
    }
}
