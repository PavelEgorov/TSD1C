package com.egorovsoft.tsd1c.FileManagers;

import android.util.Log;

import com.egorovsoft.tsd1c.MainPresenter;
import com.egorovsoft.tsd1c.data.ScanItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;


public class FileManager {
    public final static String FILEPATH = "tsd_to_1c";
    public final static String FILE = ".txt";
    private static final String TAG = "FileManager";
    private final static String FILENAME = "/1c_to_tsd.txt";

    public static void saveFile(File filesDir, String text, String fileName){
        Log.d(TAG, "saveFile: " + filesDir + FILEPATH + FILE);
        try {
            FileWriter f = new FileWriter(filesDir + MainPresenter.getInstance().getName(),
                    false);
            f.write(text);
            f.flush();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();
        //FileInputStream inputStream = new FileInputStream(myFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static String arrayToGson(ScanItems arr){
        Gson gson = new Gson();
        return gson.toJson(arr);
    }

    public static ScanItems gsonToArray(String str){
        Type itemsListType = new TypeToken<ScanItems>(){}.getType();
        ScanItems items = new Gson().fromJson(str,itemsListType);
        return items;
    }
}
