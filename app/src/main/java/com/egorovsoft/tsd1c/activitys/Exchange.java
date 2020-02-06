package com.egorovsoft.tsd1c.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.egorovsoft.tsd1c.FileManagers.FileManager;
import com.egorovsoft.tsd1c.MainPresenter;
import com.egorovsoft.tsd1c.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.widget.Toast.LENGTH_SHORT;

public class Exchange extends AppCompatActivity {
    private final static String TAG = "Exchange";
    private static final int LOADFILEREQUESTCODE = 1001;


    private TextInputLayout edtFileName;
    private Button btn_save_file;
    private Button btn_share;
    private Button btn_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        edtFileName = findViewById(R.id.edtFileName);

        btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_share");

                MainPresenter.getInstance().getFileName(edtFileName.getEditText().getText().toString());

                MainPresenter.getInstance().saveIntoFile(Environment.getExternalStorageDirectory(), MainPresenter.getInstance().getName());
                shareFile(v.getContext());
                Toast.makeText(getApplicationContext(), R.string.file_saved, LENGTH_SHORT).show();
            }
        });

        btn_save_file = findViewById(R.id.btn_save_file);
        btn_save_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_save_file");
                File file;
                file = getExternalCacheDir();
                MainPresenter.getInstance().getFileName(edtFileName.getEditText().getText().toString());

                MainPresenter.getInstance().saveIntoFile(file, MainPresenter.getInstance().getName());
                Toast.makeText(getApplicationContext(), R.string.file_saved, LENGTH_SHORT).show();
            }
        });

        btn_load = findViewById(R.id.btn_load);
        btn_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_load");

                Intent load = new Intent(Intent.ACTION_GET_CONTENT, null);
                load.setType("*/*");
                load.addCategory(Intent.CATEGORY_OPENABLE);
                Intent i = Intent.createChooser(load, "load file");
                startActivityForResult(i, LOADFILEREQUESTCODE);
            }
        });
    }

    private void shareFile(Context context) {
        Intent share = new Intent(Intent.ACTION_SEND);

        String filePath = Environment.getExternalStorageDirectory()
                + MainPresenter.getInstance().getName();

        File fileToShare = new File(filePath);

        Uri contentUri = Uri.fromFile(fileToShare);
        share.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkURI = FileProvider.getUriForFile(context,
                                            context.getApplicationContext().getPackageName() + ".provider",
                                                    fileToShare);
            share.setDataAndType(apkURI, "file/*");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.putExtra(Intent.EXTRA_STREAM, apkURI);
        } else {
            share.setDataAndType(contentUri, "file/*");
            share.putExtra(Intent.EXTRA_STREAM, contentUri);
        }
        startActivity(Intent.createChooser(share, "Share file!"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        if (requestCode == LOADFILEREQUESTCODE){

            InputStream file = null;
            try {
                file = getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            MainPresenter.getInstance().loadPositionFile(FileManager.readFile(file));
        }
    }
}
