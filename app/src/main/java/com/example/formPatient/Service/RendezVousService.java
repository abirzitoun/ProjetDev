package com.example.formPatient.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.formPatient.Entity.RendezVous;
import com.example.formPatient.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class RendezVousService {
    private SQLiteDatabase db;
    private MyDatabaseHelper dbHelper;
    public RendezVousService(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }
     public long addRendezVous(RendezVous rendezVous) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_id", rendezVous.getPatientId());
        values.put("specialty", rendezVous.getSpecialty());
        values.put("date_rendezvous", rendezVous.getDateRendezVous());
        values.put("status", rendezVous.getStatus());

        long result = db.insert("rendezvous", null, values);
        db.close();
        return result;
    }
    public List<RendezVous> getAllRendezVous() {
        List<RendezVous> rendezVousList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("rendezvous",
                new String[]{"id", "patient_id", "specialty" , "date_rendezvous", "status"},
                null, null, null, null, "date_rendezvous ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                String specialty = cursor.getString(cursor.getColumnIndexOrThrow("specialty"));
                String dateRendezVous = cursor.getString(cursor.getColumnIndexOrThrow("date_rendezvous"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                RendezVous rendezVous = new RendezVous(id, patientId, specialty,dateRendezVous,  status);
                rendezVousList.add(rendezVous);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rendezVousList;
    }


    public RendezVous getRendezVousById(int rendezVousId) {
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("rendezvous",
                new String[]{"id", "patient_id", "specialty", "date_rendezvous",  "status"},
                "id = ?", new String[]{String.valueOf(rendezVousId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            String specialty = cursor.getString(cursor.getColumnIndexOrThrow("specialty"));
            String dateRendezVous = cursor.getString(cursor.getColumnIndexOrThrow("date_rendezvous"));

            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            cursor.close();
            db.close();
            return new RendezVous(id, patientId, dateRendezVous, specialty, status);
        }

        return null;
    }


    public int updateRendezVous(RendezVous rendezVous) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", rendezVous.getPatientId());
        values.put("specialty", rendezVous.getSpecialty());
        values.put("date_rendezvous", rendezVous.getDateRendezVous());
        values.put("status", rendezVous.getStatus());

        int rowsAffected = db.update("rendezvous", values, "id = ?",
                new String[]{String.valueOf(rendezVous.getId())});

        db.close();
        return rowsAffected;
    }
    public long updateRendezVous1(RendezVous rendezVous) {
         SQLiteDatabase db = dbHelper.getWritableDatabase();

         ContentValues values = new ContentValues();
        values.put("status", rendezVous.getStatus());

         String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(rendezVous.getId()) };


        long result = db.update("rendezvous", values, selection, selectionArgs);


        Log.d("RendezVousService", "Update result: " + result);

        db.close();

         return result;
    }



    public int deleteRendezVous(int rendezVousId) {
        db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete("rendezvous", "id = ?", new String[]{String.valueOf(rendezVousId)});

        db.close();
        return rowsDeleted;
    }
    public String getPatientNameById(int patientId) {
        db = dbHelper.getReadableDatabase();
         Cursor cursor = db.query("patients",
                new String[]{"nom"},
                "id = ?",
                new String[]{String.valueOf(patientId)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
             String patientName = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
            cursor.close();
            db.close();
            return patientName;
        }
        cursor.close();
        db.close();
        return null;
    }
}
