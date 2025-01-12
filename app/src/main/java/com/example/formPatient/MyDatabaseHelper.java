package com.example.formPatient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "formPatient.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_AMBULANCE = "ambulances";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONDUCTEUR = "conducteur";
    public static final String COLUMN_DISPONIBILITE = "disponibilite";
    public static final String COLUMN_LOCALISATION = "localisation";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_DATE_TRANSPORT = "dateTransport";
    public static final String COLUMN_MATRICULE = "matricule";

    private static final String TABLE_AMBULANCE_CREATE =
            "CREATE TABLE " + TABLE_AMBULANCE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CONDUCTEUR + " TEXT NOT NULL, " +
                    COLUMN_DISPONIBILITE + " TEXT NOT NULL, " +
                    COLUMN_LOCALISATION + " TEXT NOT NULL, " +
                    COLUMN_DESTINATION + " TEXT NOT NULL, " +
                    COLUMN_DATE_TRANSPORT + " TEXT NOT NULL, " +
                    COLUMN_MATRICULE + " TEXT NOT NULL);";



    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_AMBULANCE_CREATE);
        android.util.Log.d("Database", "Table ambulances created successfully.");
        insertSampleAmbulances(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    private void insertSampleAmbulances(SQLiteDatabase db) {
        String insertAmbulance1 = "INSERT INTO " + TABLE_AMBULANCE + " (" +
                COLUMN_CONDUCTEUR + ", " + COLUMN_DISPONIBILITE + ", " +
                COLUMN_LOCALISATION + ", " + COLUMN_DESTINATION + ", " +
                COLUMN_DATE_TRANSPORT + ", " + COLUMN_MATRICULE +
                ") VALUES ('Ahmed Ben Ali', 'Unavailable', '36.8005,10.1855', 'CHU Tunis', '2024-12-15', 'ABC-1234');";

        String insertAmbulance2 = "INSERT INTO " + TABLE_AMBULANCE + " (" +
                COLUMN_CONDUCTEUR + ", " + COLUMN_DISPONIBILITE + ", " +
                COLUMN_LOCALISATION + ", " + COLUMN_DESTINATION + ", " +
                COLUMN_DATE_TRANSPORT + ", " + COLUMN_MATRICULE +
                ") VALUES ('Fatma Khlifi', 'Unavailable', '34.7406,10.7603', 'Hospital Sfax', '2024-12-18', 'DEF-5678');";

        String insertAmbulance3 = "INSERT INTO " + TABLE_AMBULANCE + " (" +
                COLUMN_CONDUCTEUR + ", " + COLUMN_DISPONIBILITE + ", " +
                COLUMN_LOCALISATION + ", " + COLUMN_DESTINATION + ", " +
                COLUMN_DATE_TRANSPORT + ", " + COLUMN_MATRICULE +
                ") VALUES ('Mohamed Ali', 'Available', '36.8665,10.1647', 'Menzah 5', '2024-12-20', 'GHI-9012');";

        String insertAmbulance4 = "INSERT INTO " + TABLE_AMBULANCE + " (" +
                COLUMN_CONDUCTEUR + ", " + COLUMN_DISPONIBILITE + ", " +
                COLUMN_LOCALISATION + ", " + COLUMN_DESTINATION + ", " +
                COLUMN_DATE_TRANSPORT + ", " + COLUMN_MATRICULE +
                ") VALUES ('Sara Bouzid', 'Available', '36.4561,10.7376', 'Hospital Nabeul', '2024-12-22', 'JKL-3456');";

        String insertAmbulance5 = "INSERT INTO " + TABLE_AMBULANCE + " (" +
                COLUMN_CONDUCTEUR + ", " + COLUMN_DISPONIBILITE + ", " +
                COLUMN_LOCALISATION + ", " + COLUMN_DESTINATION + ", " +
                COLUMN_DATE_TRANSPORT + ", " + COLUMN_MATRICULE +
                ") VALUES ('Walid Zghal', 'Unavailable', '35.8288,10.6406', 'CHU Sousse', '2024-12-23', 'MNO-7890');";

        db.execSQL(insertAmbulance1);
        db.execSQL(insertAmbulance2);
        db.execSQL(insertAmbulance3);
        db.execSQL(insertAmbulance4);
        db.execSQL(insertAmbulance5);

    }


    public boolean isUserExists(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE login = ?", new String[]{login});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
