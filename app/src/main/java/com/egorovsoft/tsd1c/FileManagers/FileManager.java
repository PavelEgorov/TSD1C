package com.egorovsoft.tsd1c.FileManagers;

import android.util.Log;
import android.widget.Toast;

import com.egorovsoft.tsd1c.MainPresenter;
import com.egorovsoft.tsd1c.data.ScanItem;
import com.egorovsoft.tsd1c.data.ScanItems;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;


public class FileManager {
    private final static String FILEPATH = "/tsd_to_1c.txt";
    private static final String TAG = "FileManager";

    public static void saveFile(File filesDir, String text){
        Log.d(TAG, "saveFile: " + filesDir + FILEPATH);
        try {
            FileWriter f = new FileWriter(filesDir + FILEPATH, false);
            f.write(text);
            f.flush();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String arrayToGson(ScanItems arr){
        Gson gson = new Gson();
        return gson.toJson(arr);
    }
}
