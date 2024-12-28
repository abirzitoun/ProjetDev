package com.example.pharmacie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class PharmacieDetails extends AppCompatActivity {
private TextView nomTv, dosageTv, prixTv,validiteTv, dateTv, timeTv;
private ImageView profileIv;
private String id ;
//data base helper
    private DbHelper dbHelper ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pharmacie_details);
        //init db
        dbHelper = new DbHelper(this);
        // get data from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("pharmacieId");

        //init view

nomTv = findViewById(R.id.nomTv);
dosageTv = findViewById(R.id.dosageTv);
prixTv = findViewById(R.id.prixTv);
validiteTv = findViewById(R.id.validiteTv);
dateTv = findViewById(R.id.dateTv);
timeTv = findViewById(R.id.timeTv);
profileIv = findViewById(R.id.profileIv);

loadDataById();

    }

    private void loadDataById() {
        // get data from db
        //query for find data by id
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\" " + id + "\"";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                // Récupérer les valeurs des colonnes
                String id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOM));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                String dosage = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DOSAGE));
                String prix = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PRIX));
                String validite = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_VALIDITE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME));

                // Convert  time to dd/mm/yy hh:mm:aa format
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(Long.parseLong(date));
                String timeAdd   = ""+ DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar);

                calendar.setTimeInMillis(Long.parseLong(time));
                String timeUpdate  = ""+ DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar);

                //set data
                nomTv.setText(nom);
                dosageTv.setText(dosage);
                prixTv.setText(prix);
                validiteTv.setText(validite);
                dateTv.setText(timeAdd);
                timeTv.setText(timeUpdate);
                if(image.equals("null")){
                    profileIv.setImageResource(R.drawable.baseline_health_and_safety_24);
                }else{
                    profileIv.setImageURI(Uri.parse(image));
                }


            } while (cursor.moveToNext());
        }
db.close();
    }

}