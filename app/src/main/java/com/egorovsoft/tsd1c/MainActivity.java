package com.egorovsoft.tsd1c;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.egorovsoft.tsd1c.activitys.Exchange;
import com.egorovsoft.tsd1c.activitys.Scanning;
import com.egorovsoft.tsd1c.activitys.Settings;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1100;

    private Button btn_scanning;
    private Button btn_exchange;
    private Button btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        if (MainPresenter.getInstance().isNeedLoadSettings()) MainPresenter.getInstance().loadSettings(this);
        MainPresenter.getInstance().updateLanguage(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn_scanning = findViewById(R.id.btn_scanning);
        btn_exchange = findViewById(R.id.btn_exchange);
        btn_settings = findViewById(R.id.btn_settings);
        
        btn_scanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_scanning");
                Intent intent;
                if (MainPresenter.getInstance().getScaningList().size() == 0){
                    intent = new Intent(MainActivity.this, Scanning.class);
                    startActivity(intent);
                }else{
                    Snackbar mySnackbar = Snackbar.make(v, R.string.sizeNotNull,Snackbar.LENGTH_SHORT);
                    mySnackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainPresenter.getInstance().clearScanningList();
                            Intent intent = new Intent(MainActivity.this, Scanning.class);
                            startActivity(intent);
                        }
                    });
                    mySnackbar.setAction("CANCEL", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, Scanning.class);
                            startActivity(intent);
                        }
                    });
                    mySnackbar.show();
                }
            }
        });
        
        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_exchange");
                Intent intent = new Intent(MainActivity.this, Exchange.class);
                startActivity(intent);
            }
        });
        
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_settings");
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(intent, MainPresenter.RESULT_CODE_SETTINGS);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            MainPresenter.getInstance().setHaveSavePermission(false);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                       },
                        PERMISSION_REQUEST_CODE);
            }
        }else MainPresenter.getInstance().setHaveSavePermission(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: resultCode: " + resultCode + "; requestCode: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == MainPresenter.RESULT_CODE_SETTINGS) {
            Log.d(TAG, "onActivityResult: RESULT_CODE_SETTINGS");

            MainPresenter.getInstance().restartActivity(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MainPresenter.getInstance().setHaveSavePermission(true);
            }
        }
    }
}
