package com.example.formPatient.Service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import com.example.formPatient.Entity.Ambulance;
import com.example.formPatient.MyDatabaseHelper;
import java.util.ArrayList;
import java.util.List;
public class AmbulanceService {
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public AmbulanceService(Context context) {
        this.dbHelper = new MyDatabaseHelper(context);
    }
    private void ensureDatabaseIsOpen() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }
    @SuppressLint("Range")
    public Ambulance getAmbulanceById(int ambulanceId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Ambulance ambulance = null;

        String query = "SELECT * FROM ambulances WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ambulanceId)});
        if (cursor != null && cursor.moveToFirst()) {
            ambulance = new Ambulance();
            ambulance.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            ambulance.setConducteur(cursor.getString(cursor.getColumnIndexOrThrow("conducteur")));
            ambulance.setMatricule(cursor.getString(cursor.getColumnIndexOrThrow("matricule")));
            ambulance.setDisponibilite(cursor.getString(cursor.getColumnIndexOrThrow("disponibilite")));
            String localisationString = cursor.getString(cursor.getColumnIndexOrThrow("localisation"));
            ambulance.setDestination(cursor.getString(cursor.getColumnIndexOrThrow("destination")));
            ambulance.setDateTransport(cursor.getString(cursor.getColumnIndexOrThrow("dateTransport")));
            cursor.close();
        }
        return ambulance;
    }
    @SuppressLint("Range")
    public List<Ambulance> getAllAmbulances() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Ambulance> ambulanceList = new ArrayList<>();
        String query = "SELECT * FROM ambulances";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ambulance ambulance = new Ambulance();
                ambulance.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                ambulance.setConducteur(cursor.getString(cursor.getColumnIndexOrThrow("conducteur")));
                ambulance.setDisponibilite(cursor.getString(cursor.getColumnIndexOrThrow("disponibilite")));
                ambulance.setLocalisation(cursor.getString(cursor.getColumnIndexOrThrow("localisation")));
                ambulance.setDestination(cursor.getString(cursor.getColumnIndexOrThrow("destination")));
                ambulance.setDateTransport(cursor.getString(cursor.getColumnIndexOrThrow("dateTransport")));
                ambulance.setMatricule(cursor.getString(cursor.getColumnIndexOrThrow("matricule")));
                ambulanceList.add(ambulance);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ambulanceList;
    }

    @SuppressLint("Range")
    public List<Ambulance> getDisponibiliteAndMatricule() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Ambulance> ambulanceList = new ArrayList<>();
        String query = "SELECT disponibilite, matricule,conducteur FROM ambulances";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ambulance ambulance = new Ambulance();
                ambulance.setDisponibilite(cursor.getString(cursor.getColumnIndexOrThrow("disponibilite")));
                ambulance.setMatricule(cursor.getString(cursor.getColumnIndexOrThrow("matricule")));
                ambulance.setConducteur(cursor.getString(cursor.getColumnIndexOrThrow("conducteur")));
                ambulanceList.add(ambulance);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ambulanceList;
    }
    public long addAmbulance(Ambulance ambulance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("conducteur", ambulance.getConducteur());
        values.put("matricule", ambulance.getMatricule());
        values.put("disponibilite", ambulance.getDisponibilite());
        values.put("destination", ambulance.getDestination());
        values.put("localisation", ambulance.getLocalisation());
        values.put("dateTransport", ambulance.getDateTransport());
        long result = db.insert("ambulances", null, values);
        db.close();
        return result;
    }
     public int updateAmbulance(Ambulance ambulance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("conducteur", ambulance.getConducteur());
        values.put("matricule", ambulance.getMatricule());
        values.put("destination", ambulance.getDestination());
        values.put("dateTransport", ambulance.getDateTransport());
        int result = db.update("ambulances", values, "id = ?", new String[]{String.valueOf(ambulance.getId())});
        db.close();
        return result;
    }
    public int updateAmbulanceLocationAndDisponibilite(int ambulanceId, Location location, String disponibilite) {
        // Validate inputs
        if (ambulanceId <= 0) {
            Log.e("AmbulanceService", "Invalid ambulance ID.");
            return 0;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e("AmbulanceService", "Database not writable.");
            return 0;
        }

        ContentValues values = new ContentValues();

        // Update location if provided
        if (location != null) {
            String locationString = location.getLatitude() + "," + location.getLongitude();
            values.put("localisation", locationString);
        }

        // Update disponibilite if provided
        if (disponibilite != null && !disponibilite.isEmpty()) {
            values.put("disponibilite", disponibilite);
        }

        // Perform update query
        int result = 0;
        try {
            result = db.update("ambulances", values, "id = ?", new String[]{String.valueOf(ambulanceId)});
        } catch (Exception e) {
            Log.e("AmbulanceService", "Error updating ambulance: " + e.getMessage());
        } finally {
            db.close();
        }

        return result;
    }
    public int deleteAmbulance(int ambulanceId) {
        ensureDatabaseIsOpen();
        int result = database.delete("ambulances", "id = ?", new String[]{String.valueOf(ambulanceId)});
        database.close();
        return result;
    }
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
