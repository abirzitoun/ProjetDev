package com.example.formPatient;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formPatient.Entity.Patient;
import com.example.formPatient.Service.PatientService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
public class PatientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Patient> patientList;
     private PatientService patientService;
    private PatientAdapter patientAdapter;
    private MyDatabaseHelper dbHelper;
    private Context context;
    private TextInputEditText search_with_Name;
    private SQLiteDatabase database;
    ImageView  buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        dbHelper = new MyDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        ImageView imageSettings = findViewById(R.id.image_settings);

        imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PatientActivity.this, RendezVousActivity.class);
                startActivity(intent);
            }
        });
        search_with_Name = findViewById(R.id.search_with_Name);

        search_with_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPatientsByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
             }
        });


        buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PatientActivity.this, AddPatientActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewPatients);
        ImageView registerImageView = findViewById(R.id.registerImageView);
        registerImageView.setOnClickListener(v -> {
            Intent intent = new Intent(PatientActivity.this, PayActivity.class);
            startActivity(intent);
        });
        patientService = new PatientService(this);
        allPatients();
    }

    private void allPatients(){
        List<Patient> patients=patientService.showAllPatient();
        if (patientService != null){
            PatientAdapter adapter =new PatientAdapter(this,patients);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
         }else {
            Toast.makeText(this,"Data not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchPatientsByName(String query) {
        List<Patient> filteredList = patientService.searchPatientsByName(query);

        if (patientAdapter == null) {
            patientAdapter = new PatientAdapter(this, new ArrayList<>());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(patientAdapter);
        }

        if (filteredList != null && !filteredList.isEmpty()) {
            patientAdapter.updateData(filteredList);
        } else {
            patientAdapter.updateData(new ArrayList<>());
        }
    }


}
