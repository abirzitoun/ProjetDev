package com.example.formPatient.Service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.formPatient.Entity.Patient;
import com.example.formPatient.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class PatientService implements IPatientDao{
    private MyDatabaseHelper dbHelper;

    private IPatientDao iPatientDao;
    private SQLiteDatabase database;

    public PatientService(Context context){
        this.dbHelper =new MyDatabaseHelper(context);
    }
    public PatientService(){
     }
    private void ensureDatabaseIsOpen() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }
    @SuppressLint("Range")
    public Patient getPatientById(int employeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Patient patient = null;
        Cursor cursor = db.rawQuery("SELECT * FROM patients WHERE id = ?", new String[]{String.valueOf(employeId)});
        if (cursor != null && cursor.moveToFirst()) {
            patient = new Patient();
            patient.setId(cursor.getInt(cursor.getColumnIndex("id")));
            patient.setNom(cursor.getString(cursor.getColumnIndex("nom")));
            patient.setPrenom(cursor.getString(cursor.getColumnIndex("prenom")));
            patient.setNumTele(cursor.getLong(cursor.getColumnIndex("numTele")));
            patient.setDateNaissance(cursor.getString(cursor.getColumnIndex("dateNaissance")));
            patient.setDateCreation(cursor.getString(cursor.getColumnIndex("dateCreation")));
            patient.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
            patient.setSang(cursor.getString(cursor.getColumnIndex("sang")));
            patient.setAllergie(cursor.getString(cursor.getColumnIndex("allergie")));
            cursor.close();
        }
        return patient;
    }
    public List<Patient> searchPatientsByName(String name) {
        List<Patient> filteredPatients = new ArrayList<>();
        for (Patient patient : showAllPatient()) {
            if (patient.getNom().toLowerCase().contains(name.toLowerCase())) {
                filteredPatients.add(patient);
            }
        }
        return filteredPatients;
    }
    @SuppressLint("Range")
    @Override
    public List<Patient> showAllPatient() {

         SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Patient> patientList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM patients", null);
        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setId(cursor.getInt(cursor.getColumnIndex("id")));
                patient.setNom(cursor.getString(cursor.getColumnIndex("nom")));
                patient.setPrenom(cursor.getString(cursor.getColumnIndex("prenom")));
                patient.setNumTele(cursor.getLong(cursor.getColumnIndex("numTele")));
                patient.setDateNaissance(cursor.getString(cursor.getColumnIndex("dateNaissance")));
                patient.setDateCreation(cursor.getString(cursor.getColumnIndex("dateCreation")));
                patient.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                patient.setSang(cursor.getString(cursor.getColumnIndex("sang")));
                patient.setAllergie(cursor.getString(cursor.getColumnIndex("allergie")));
                patientList.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return patientList;
    }

    @Override
    public long addPatient(Patient patient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("prenom", patient.getPrenom());
        values.put("nom", patient.getNom());
        values.put("numTele", patient.getNumTele());
        values.put("dateNaissance", patient.getDateNaissance());
        values.put("dateCreation", patient.getDateCreation());
        values.put("sexe", patient.getSexe());
        values.put("sang", patient.getSang());
        values.put("allergie", patient.getAllergie());

         long result = db.insert("patients", null, values);

         db.close();

        return result;
    }


    @Override
    public int updatePatient(Patient patient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prenom", patient.getPrenom());
        values.put("nom", patient.getNom());
        values.put("numTele", patient.getNumTele());
        values.put("dateNaissance", patient.getDateNaissance());
        values.put("sexe", patient.getSexe());
        values.put("sang", patient.getSang());
        values.put("allergie", patient.getAllergie());
        int rowsAffected = db.update("patients", values, "id = ?", new String[]{String.valueOf(patient.getId())});
        db.close();
        return rowsAffected;
    }

    @Override
    public int deletePatient(int patientId) {
        ensureDatabaseIsOpen();
        String selection = MyDatabaseHelper.COLUMN_PATIENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(patientId)};

        return database.delete(MyDatabaseHelper.TABLE_PATIENT, selection, selectionArgs);
    }

    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
