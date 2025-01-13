package com.example.formPatient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientAdd extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_add);


        String[] type = new String[] {"A+" , "A-", "B+" , "B-" , "AB+" , "AB-" , "O+" , "O-"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this ,R.layout.drop_down , type);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.bloodType);
        autoCompleteTextView.setAdapter(adapter3);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView3, View view, int position, long id) {
                Toast.makeText(PatientAdd.this , autoCompleteTextView.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });



        String[] allergies = new String[] {
                "Peanuts", "Shellfish", "Dairy", "Eggs", "Wheat", "Soy", "Latex",
                "Dust", "Pollen", "Penicillin", "Mold", "Bee Stings", "Aspirin",
                "Nickel", "Chocolates", "Fish", "Tree Nuts", "Sesame", "Garlic"
        };
        ArrayAdapter<String> allergiesAdapter = new ArrayAdapter<>(this, R.layout.drop_down, allergies);

        AutoCompleteTextView allergiesTextView = findViewById(R.id.allergies);
        allergiesTextView.setAdapter(allergiesAdapter);
        allergiesTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(PatientAdd.this, "Selected Allergy: " + allergiesTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        String[] specialties = new String[] {
                "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology", "Psychiatry",
                "Gynecology", "General Surgery", "Endocrinology", "Rheumatology", "Gastroenterology",
                "Oncology", "Pulmonology", "Urology", "Ophthalmology", "ENT", "Pathology", "Infectious Disease",
                "Plastic Surgery", "Nephrology"
        };
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(this, R.layout.drop_down, specialties);


    }
}
