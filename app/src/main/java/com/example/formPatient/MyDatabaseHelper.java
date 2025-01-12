package com.example.formPatient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "formPatient.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PATIENT = "patients";
    public static final String COLUMN_PATIENT_ID = "id";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_PRENOM = "prenom";
    public static final String COLUMN_NUM_TELE = "numTele";
    public static final String COLUMN_DATE_NAISSANCE = "dateNaissance";
    public static final String COLUMN_DATE_CREATION = "dateCreation";
    public static final String COLUMN_SEXE = "sexe";
    public static final String COLUMN_TYPE_SANG = "sang";
    public static final String COLUMN_ALLERGIE = "allergie";



    public static final String TABLE_RENDEZVOUS = "rendezvous";
    public static final String COLUMN_RENDEZVOUS_ID = "id";
    public static final String COLUMN_PATIENT_ID_FK = "patient_id";
    public static final String COLUMN_RENDEZVOUS_DATE = "date_rendezvous";
    public static final String COLUMN_SPECIALTY = "specialty";
    public static final String COLUMN_STATUS = "status";


    public static final String TABLE_USER = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_PRENOM = "prenom";
    public static final String COLUMN_USER_NOM = "nom";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_LOGIN = "login";
    public static final String COLUMN_USER_PWD = "pwd";
    public static final String COLUMN_USER_ACTIF = "actif";
    public static final String COLUMN_USER_VALSYNC = "valsync";


    private static final String TABLE_PATIENT_CREATE =
            "CREATE TABLE " + TABLE_PATIENT + " (" +
                    COLUMN_PATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOM + " TEXT NOT NULL, " +
                    COLUMN_PRENOM + " TEXT NOT NULL, " +
                    COLUMN_NUM_TELE + " INTEGER, " +
                    COLUMN_DATE_NAISSANCE + " TEXT, " +
                    COLUMN_DATE_CREATION + " TEXT, " +
                    COLUMN_SEXE + " TEXT, " +
                    COLUMN_TYPE_SANG + " TEXT, " +
                    COLUMN_ALLERGIE + " TEXT); ";

    private static final String TABLE_RENDEZVOUS_CREATE =
            "CREATE TABLE " + TABLE_RENDEZVOUS + " (" +
                    COLUMN_RENDEZVOUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PATIENT_ID_FK + " INTEGER, " +
                    COLUMN_RENDEZVOUS_DATE + " TEXT NOT NULL, " +
                    COLUMN_SPECIALTY + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_PATIENT_ID_FK + ") REFERENCES " +
                    TABLE_PATIENT + "(" + COLUMN_PATIENT_ID + ") ON DELETE CASCADE);";


    private static final String TABLE_USER_CREATE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_PRENOM + " TEXT NOT NULL, " +
                    COLUMN_USER_NOM + " TEXT NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_USER_LOGIN + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_USER_PWD + " TEXT NOT NULL, " +
                    COLUMN_USER_ACTIF + " INTEGER NOT NULL DEFAULT 1, " +
                    COLUMN_USER_VALSYNC + " INTEGER DEFAULT 0);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PATIENT_CREATE);
        db.execSQL(TABLE_RENDEZVOUS_CREATE);
        db.execSQL(TABLE_USER_CREATE);
        insertSamplePatients(db);
        insertSampleRendezVous(db);
        insertSampleUsers(db);

        db.execSQL("PRAGMA foreign_keys = ON;");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENDEZVOUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    private void insertSamplePatients(SQLiteDatabase db) {
        String insertPatient1 = "INSERT INTO " + TABLE_PATIENT + " (" +
                COLUMN_NOM + ", " + COLUMN_PRENOM + ", " +
                COLUMN_NUM_TELE + ", " + COLUMN_DATE_NAISSANCE + ", " +
                COLUMN_DATE_CREATION + ", " + COLUMN_SEXE + ", " +
                COLUMN_TYPE_SANG + ", " + COLUMN_ALLERGIE +
                ") VALUES ('Doe', 'John', 1234567890, '1980-01-01', '2023-11-20', 'Male', 'O+', 'Peanuts');";

        String insertPatient2 = "INSERT INTO " + TABLE_PATIENT + " (" +
                COLUMN_NOM + ", " + COLUMN_PRENOM + ", " +
                COLUMN_NUM_TELE + ", " + COLUMN_DATE_NAISSANCE + ", " +
                COLUMN_DATE_CREATION + ", " + COLUMN_SEXE + ", " +
                COLUMN_TYPE_SANG + ", " + COLUMN_ALLERGIE + ") VALUES ('Smith', 'Jane', 9876543210, '1992-06-15', '2023-11-20', 'Female', 'A-', 'Peanuts');";

        String insertPatient3 = "INSERT INTO " + TABLE_PATIENT + " (" +
                COLUMN_NOM + ", " + COLUMN_PRENOM + ", " +
                COLUMN_NUM_TELE + ", " + COLUMN_DATE_NAISSANCE + ", " +
                COLUMN_DATE_CREATION + ", " + COLUMN_SEXE + ", " +
                COLUMN_TYPE_SANG + ", " + COLUMN_ALLERGIE +
                ") VALUES ('Brown', 'Michael', 1231231234, '1975-03-30', '2023-11-20', 'Male', 'B+', 'Peanuts');";

        String insertPatient4 = "INSERT INTO " + TABLE_PATIENT + " (" +
                COLUMN_NOM + ", " + COLUMN_PRENOM + ", " +
                COLUMN_NUM_TELE + ", " + COLUMN_DATE_NAISSANCE + ", " +
                COLUMN_DATE_CREATION + ", " + COLUMN_SEXE + ", " +
                COLUMN_TYPE_SANG + ", " + COLUMN_ALLERGIE + ") VALUES ('Williams', 'Emily', 4564564567, '1988-09-12', '2023-11-20', 'Female', 'AB+', 'Shellfish');";

        String insertPatient5 = "INSERT INTO " + TABLE_PATIENT + " (" +
                COLUMN_NOM + ", " + COLUMN_PRENOM + ", " +
                COLUMN_NUM_TELE + ", " + COLUMN_DATE_NAISSANCE + ", " +
                COLUMN_DATE_CREATION + ", " + COLUMN_SEXE + ", " +
                COLUMN_TYPE_SANG + ", " + COLUMN_ALLERGIE +
                ") VALUES ('Taylor', 'Anna', 7897897890, '1995-11-05', '2023-11-20', 'Female', 'O-', 'Dust');";


        db.execSQL(insertPatient1);
        db.execSQL(insertPatient2);
        db.execSQL(insertPatient3);
        db.execSQL(insertPatient4);
        db.execSQL(insertPatient5);
    }


    private void insertSampleRendezVous(SQLiteDatabase db) {
        String insertRendezVous1 = "INSERT INTO " + TABLE_RENDEZVOUS + " (" +
                COLUMN_PATIENT_ID_FK + ", " + COLUMN_RENDEZVOUS_DATE + ", " +
                COLUMN_SPECIALTY + ", " + COLUMN_STATUS + ") VALUES (1, '2024-01-15', 'Cardiology', 'Pending');";

        String insertRendezVous2 = "INSERT INTO " + TABLE_RENDEZVOUS + " (" +
                COLUMN_PATIENT_ID_FK + ", " + COLUMN_RENDEZVOUS_DATE + ", " +
                COLUMN_SPECIALTY + ", " + COLUMN_STATUS + ") VALUES (2, '2024-02-20', 'Dermatology', 'Confirmed');";

        String insertRendezVous3 = "INSERT INTO " + TABLE_RENDEZVOUS + " (" +
                COLUMN_PATIENT_ID_FK + ", " + COLUMN_RENDEZVOUS_DATE + ", " +
                COLUMN_SPECIALTY + ", " + COLUMN_STATUS + ") VALUES (3, '2024-03-10', 'Neurology', 'Pending');";

        String insertRendezVous4 = "INSERT INTO " + TABLE_RENDEZVOUS + " (" +
                COLUMN_PATIENT_ID_FK + ", " + COLUMN_RENDEZVOUS_DATE + ", " +
                COLUMN_SPECIALTY + ", " + COLUMN_STATUS + ") VALUES (1, '2024-01-25', 'Pediatrics', 'Confirmed');";

        String insertRendezVous5 = "INSERT INTO " + TABLE_RENDEZVOUS + " (" +
                COLUMN_PATIENT_ID_FK + ", " + COLUMN_RENDEZVOUS_DATE + ", " +
                COLUMN_SPECIALTY + ", " + COLUMN_STATUS + ") VALUES (4, '2024-04-05', 'Psychiatry', 'Confirmed');";

        db.execSQL(insertRendezVous1);
        db.execSQL(insertRendezVous2);
        db.execSQL(insertRendezVous3);
        db.execSQL(insertRendezVous4);
        db.execSQL(insertRendezVous5);
    }


    private void insertSampleUsers(SQLiteDatabase db) {
        String insertUser1 = "INSERT INTO " + TABLE_USER + " (" +
                COLUMN_USER_PRENOM + ", " + COLUMN_USER_NOM + ", " +
                COLUMN_USER_EMAIL + ", " + COLUMN_USER_LOGIN + ", " +
                COLUMN_USER_PWD + ", " + COLUMN_USER_ACTIF + ", " +
                COLUMN_USER_VALSYNC + ") VALUES ('ayoub', 'ayoub', 'ayoub@gmail.com', 'ayoub', 'ayoub', 1, 0);";
        db.execSQL(insertUser1);

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
