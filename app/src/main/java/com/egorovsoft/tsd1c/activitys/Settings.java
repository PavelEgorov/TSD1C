package com.egorovsoft.tsd1c.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.egorovsoft.tsd1c.MainPresenter;
import com.egorovsoft.tsd1c.R;

public class Settings extends AppCompatActivity {
    private final static String TAG = "Settings";

    private Button btn_back;
    private RadioButton rb_us;
    private RadioButton rb_ru;
    private EditText fileDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rb_us = findViewById(R.id.rb_english);
        rb_ru = findViewById(R.id.rb_russian);

        fileDir = findViewById(R.id.edTxt_fileDir);
        fileDir.setVisibility(View.GONE);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_back ");
                onBackPressed();
            }
        });

        loadSettings();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        saveSettings();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    private void saveSettings(){
        Log.d(TAG, "saveSettings: ");

        if (rb_ru.isChecked()) MainPresenter.getInstance().setCurrentLanguage(MainPresenter.LANGUAGE_RU);
        else MainPresenter.getInstance().setCurrentLanguage(MainPresenter.LANGUAGE_US);

        MainPresenter.getInstance().setFileDir(fileDir.getText().toString());
        MainPresenter.getInstance().saveSettings(this);
    }

    private void loadSettings(){
        Log.d(TAG, "loadSettings: ");

        fileDir.setText(MainPresenter.getInstance().getFileDir());

        if (MainPresenter.getInstance().getCurrentLanguage() == MainPresenter.LANGUAGE_RU) {
            rb_ru.setChecked(true);
            rb_us.setChecked(false);
        }
        else {
            rb_us.setChecked(true);
            rb_ru.setChecked(false);
        }
    }
}
