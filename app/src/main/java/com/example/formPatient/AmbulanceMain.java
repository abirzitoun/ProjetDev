package com.example.formPatient;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AmbulanceMain extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

    }
}
