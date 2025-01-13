package com.example.pharmacie;

public class Constants {
    //database or db name
    public static final String DATABASE_NAME = "pharmacie_db";
    //database version
    public static final int DATABASE_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "pharmacie_table";
    //table column or field name
    public static final String C_ID = "id";
    public static final String C_IMAGE = "Image" ;
    public static final String C_NOM = "nom";
    public static final String C_DOSAGE = "dosage";
    public static final String C_PRIX = "prix";
    public static final String C_QUANTITY = "quantity";
    public static final String C_VALIDITE = "validite";
    public static final String C_ADDED_DATE = "date";
    public static final String C_UPDATED_TIME = "time";
    //Requête SQL pour créer la table :
    public static final String CREATE_TABLE ="CREATE TABLE " + TABLE_NAME +"("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_IMAGE + " TEXT,"
            + C_NOM + " TEXT,"
            + C_DOSAGE + " TEXT,"
            + C_PRIX + " TEXT,"
            + C_QUANTITY + " TEXT,"
            + C_VALIDITE + " TEXT,"
            + C_ADDED_DATE + " TEXT,"
            + C_UPDATED_TIME + " TEXT"
            + ")";

}
