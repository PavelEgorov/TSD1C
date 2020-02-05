package com.egorovsoft.tsd1c;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.egorovsoft.tsd1c.FileManagers.FileManager;
import com.egorovsoft.tsd1c.data.ScanItem;
import com.egorovsoft.tsd1c.data.ScanItems;
import com.egorovsoft.tsd1c.observers.Publisher;
import com.egorovsoft.tsd1c.preference.SPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class MainPresenter {
    private static final Object sync = new Object();
    private static MainPresenter instance;

    private final static String TAG = "MainPresenter";

    public static final int LANGUAGE_US = 1001;
    public static final int LANGUAGE_RU = 1002;

    public static final int RESULT_CODE_SETTINGS = 101;

    private int currentLanguage;
    private Thread threadSettings;
    private Handler handler; /// ссылка на основной поток

    private Locale  locale;
    private Configuration configuration;
    private boolean needLoadSettings;
    private ScanItems scaningList;

    private String fileDir;

    private boolean haveSavePermission;

    private MainPresenter(){
        Log.d(TAG, instance==null ? "instance = null" : instance.toString() + " MainPresenter: ");
        currentLanguage = MainPresenter.LANGUAGE_US;
        handler = new Handler();
        locale = new Locale("en");
        configuration = new Configuration();
        needLoadSettings = true;
        scaningList = new ScanItems();
        haveSavePermission = false;
    }

    public static MainPresenter getInstance(){
        Log.d(TAG, instance==null ? "instance = null" : instance.toString() + " getInstance: ");
        synchronized (sync){
            if (instance == null) instance = new MainPresenter();

            return instance;
        }
    }

    public void updateLanguage(Context context){
        Log.d(TAG, instance.toString() + " updateLanguage: ");
        if (MainPresenter.getInstance().getCurrentLanguage()== MainPresenter.LANGUAGE_US) locale = new Locale("en");
        if (MainPresenter.getInstance().getCurrentLanguage() == MainPresenter.LANGUAGE_RU) locale = new Locale("ru");

        Locale.setDefault(locale);
        configuration.setLocale(locale);
        context.getResources().updateConfiguration(configuration, null);
    }

    public int getCurrentLanguage() {
        Log.d(TAG, instance.toString() + " getCurrentLanguage: " + currentLanguage);
        return currentLanguage;
    }

    public void setCurrentLanguage(int currentLanguage) {
        Log.d(TAG, instance.toString() + " setCurrentLanguage: " + currentLanguage);
        synchronized (sync) {
            this.currentLanguage = currentLanguage;
        }
    }

    private void dropThread(Thread tr){
        Log.d(TAG, instance.toString() + " dropThread: " + tr.toString());

        if (tr == null) return;

        Thread dummy = tr;
        tr = null;
        dummy.interrupt();
    }

    public void saveSettings(final Context context){
        Log.d(TAG, instance.toString() + " saveSettings: ");
        Runnable runnableSettings = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, instance.toString() + " saveSettings run: " + threadSettings.toString());
                SPreference.getInstance().setSharedPreference(context, SPreference.SETTINGS);

                SPreference.getInstance().saveInt(SPreference.LANGUAGE, currentLanguage);
                Log.d(TAG, "saveSettings run: currentLanguage: " + currentLanguage);

                SPreference.getInstance().saveString(SPreference.FILEDIR, getFileDir());
                Log.d(TAG, "saveSettings run: file dirrectory: " + getFileDir());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, instance.toString() + " saveSettings ended: ");

                        /// context.getApplicationContext() чтобы программа не пыталась показать сообщение у закрытого активити
                        Toast.makeText(context, context.getApplicationContext().getResources().getString(R.string.save_settings), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        if (threadSettings != null) dropThread(threadSettings);

        threadSettings = new Thread(runnableSettings);
        threadSettings.setDaemon(true);
        threadSettings.start();
    }

    public void loadSettings(Context context){
        Log.d(TAG, instance.toString() + " loadSettings: ");

        SPreference.getInstance().setSharedPreference(context, SPreference.SETTINGS);

        setCurrentLanguage(SPreference.getInstance().loadInt(SPreference.LANGUAGE));
        setFileDir(SPreference.getInstance().loadString(SPreference.FILEDIR));
        Log.d(TAG, "loadSettings: currentLanguage: " + currentLanguage);
        Log.d(TAG, "loadSettings: fileDir: " + fileDir);

        setNeedLoadSettings(false);
    }

    public void restartActivity(Activity context){
        Log.d(TAG, instance.toString() + " restartActivity: ");
        Intent intent = new Intent(context.getApplicationContext(), context.getClass());
        context.startActivity(intent);
        context.finish();
    }

    public boolean isNeedLoadSettings() {
        Log.d(TAG, instance.toString() + " isNeedLoadSettings: " + needLoadSettings);
        return needLoadSettings;
    }

    public void setNeedLoadSettings(boolean needLoadSettings) {
        Log.d(TAG, instance.toString() + " setNeedLoadSettings: " + needLoadSettings);
        synchronized (sync) {
            this.needLoadSettings = needLoadSettings;
        }
    }

    public ArrayList<ScanItem> getScaningList() {
        Log.d(TAG, instance.toString() + " getScaningList: ");
        return scaningList.getList();
    }

    public void addToList(String text) {
        Log.d(TAG, instance.toString() + " addToList: ");
        int index = -1;

        for (ScanItem item:
             scaningList.getList()) {
            if (item.getScanCode().equals(text)) {
                index = scaningList.getList().indexOf(item);
                break;
            }
        }

        if (index == -1){
            ScanItem newItem = new ScanItem();
            newItem.setCount(1);
            newItem.setScanCode(text);
            newItem.setName("");

            scaningList.getList().add(newItem);
        }else
            scaningList.getList().get(index).setCount(scaningList.getList().get(index).getCount() + 1);

        Publisher.getInstance().notifyUpdateList(scaningList.getList());
    }

    public void saveIntoFile(File filesDir) {
        Log.d(TAG, instance.toString() + " saveIntoFile: ");
        if (!isHaveSavePermission()) return;

        FileManager.saveFile(filesDir, FileManager.arrayToGson(scaningList));
    }

    public boolean isHaveSavePermission() {
        Log.d(TAG, instance.toString() + " isHaveSavePermission: " + haveSavePermission);
        return haveSavePermission;
    }

    public void setHaveSavePermission(boolean havePermission) {
        Log.d(TAG, instance.toString() + " haveSavePermission: " + havePermission);
        synchronized (sync){
            this.haveSavePermission = havePermission;
        }
    }

    public void close(){
        Log.d(TAG, instance.toString() + " close: ");

        dropThread(threadSettings);
        handler = null;
    }

    public String getFileDir() {
        Log.d(TAG, instance.toString() + " getFileDir: ");
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        Log.d(TAG, instance.toString() + " setFileDir: ");
        synchronized (sync) {
            this.fileDir = fileDir;
        }
    }

    public void clearScanningList() {
        Log.d(TAG, instance.toString() + " clearScanningList: ");

        synchronized (sync) {
            scaningList.getList().clear();
        }
    }

    public void loadPositionFile(String file) {
        scaningList = FileManager.gsonToArray(file);
    }
}
