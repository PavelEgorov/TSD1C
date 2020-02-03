package com.egorovsoft.tsd1c.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.egorovsoft.tsd1c.MainPresenter;
import com.egorovsoft.tsd1c.R;
import com.egorovsoft.tsd1c.RecycleView.ItemAdapter;
import com.egorovsoft.tsd1c.data.ScanItem;
import com.egorovsoft.tsd1c.observers.Observer;
import com.egorovsoft.tsd1c.observers.Publisher;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Scanner;

public class Scanning extends AppCompatActivity implements Observer {
    private final static String TAG = "Scanning";

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<ScanItem> list;
    private Button btn_add;
    private TextInputLayout tv_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        tv_code = findViewById(R.id.til_scancode);
        tv_code.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: s: " + s.toString());
                s = "";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: s: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s.toString());
                if (!s.toString().equals("")) MainPresenter.getInstance().addToList(tv_code.getEditText().getText().toString());
                s.clear();
            }
        });
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_add");
                MainPresenter.getInstance().addToList(tv_code.getEditText().getText().toString());
            }
        });

        list = new ArrayList<>();
        initRecyclerView();

        Publisher.getInstance().subscribe(this);

        }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        Publisher.getInstance().unsubscribe(this);
        super.onStop();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        update(MainPresenter.getInstance().getScaningList());

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_of_scancodes);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemAdapter(list);
        recyclerView.setAdapter(adapter);
   }

   public void updateList(ArrayList<ScanItem> newList){
       update(newList);
       adapter.notifyDataSetChanged();
   }

    private void update(ArrayList<ScanItem> newList) {
        Log.d(TAG, "updateList: ");
        this.list.clear();
        this.list.addAll(newList);
    }
}
