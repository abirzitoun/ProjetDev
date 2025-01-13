package com.example.clinic;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "laboratory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "labs";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Method to insert data into the database
    public long addSample(String name, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);

        // Insert data into the database and return the row ID
        return db.insert(TABLE_NAME, null, values);
    }

    // Method to fetch all samples from the database
    public ArrayList<Lab> getAllLabs() {
        ArrayList<Lab> labsList = new ArrayList<>();

        // SQL query to get all labs
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Fetch the data from the cursor
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                // Add Lab object to the list
                Lab lab = new Lab(name, description, date);
                labsList.add(lab);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return labsList;
    }
    public boolean deleteLab(String labName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{labName});
        db.close();
        return result > 0;
    }
    public void updateLab(Lab lab) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, lab.getName());  // Use the defined constant for name
        values.put(COLUMN_DESCRIPTION, lab.getDescription());
        values.put(COLUMN_DATE, lab.getDate());

        // Update based on the name (ensure name is unique in your database)
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{lab.getName()});
        db.close();

        if (rowsAffected <= 0) {
            Log.e("Database", "Failed to update lab with name: " + lab.getName());
        }
    }





}
