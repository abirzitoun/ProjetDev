package com.example.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LabAdapter labAdapter;
    private ArrayList<Lab> labsList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;
    Button scannBn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.labRecyView);
        fab = findViewById(R.id.fab);
        dbHelper = new DatabaseHelper(this);
        labsList = dbHelper.getAllLabs();

        labAdapter = new LabAdapter(labsList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(labAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddLabActivity.class));
            }
        });
        scannBn = findViewById(R.id.scanner);

        scannBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        labsList.clear();
        labsList.addAll(dbHelper.getAllLabs());
        labAdapter.notifyDataSetChanged();
    }

}
