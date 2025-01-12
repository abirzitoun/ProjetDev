package com.example.formPatient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formPatient.Entity.Ambulance;
import com.example.formPatient.Service.AmbulanceService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditAmbulanceActivity extends AppCompatActivity {

    private AutoCompleteTextView editTextDriverName;
    private AutoCompleteTextView editTextMatricule;
    private EditText editTextDestination, editTextDate;
    private MaterialButton buttonSave;

    private int ambulanceId;
    private AmbulanceService ambulanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ambulance);


        editTextDriverName = findViewById(R.id.editTextAmbulanceName);
        editTextMatricule = findViewById(R.id.editTextMatricule);
        editTextDestination = findViewById(R.id.editTextDestination);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSave = findViewById(R.id.buttonSave);


        ambulanceService = new AmbulanceService(this);

        // Get Ambulance ID from Intent
        Intent intent = getIntent();
        ambulanceId = intent.getIntExtra("Ambulance_ID", -1);

        if (ambulanceId != -1) {
            loadAmbulanceData(ambulanceId);
        }


        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    EditAmbulanceActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        editTextDate.setText(formattedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Set up Save button click listener
        buttonSave.setOnClickListener(v -> saveAmbulanceData());

        // Add predefined drivers for AutoCompleteTextView
        String[] predefinedConducteurs = new String[]{
                "Mohamed Ali",
                "Sara Bouzid",
                "Fatma Khlifi",
                "Walid Zghal",
                "Ahmed Ben Ali"
        };

        List<Ambulance> ambulanceList = ambulanceService.getDisponibiliteAndMatricule();
        List<String> matchedConducteurs = new ArrayList<>();
        for (Ambulance ambulance : ambulanceList) {
            for (String predefinedConducteur : predefinedConducteurs) {
                if (predefinedConducteur.equalsIgnoreCase(ambulance.getConducteur()) &&
                        "Available".equalsIgnoreCase(ambulance.getDisponibilite())) {
                    matchedConducteurs.add(ambulance.getConducteur());
                }
            }
        }

        ArrayAdapter<String> conducteurAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, matchedConducteurs);
        editTextDriverName.setAdapter(conducteurAdapter);

        editTextDriverName.setOnItemClickListener((adapterView, view, position, id) ->
                Toast.makeText(EditAmbulanceActivity.this,
                        "Selected Driver: " + editTextDriverName.getText().toString(),
                        Toast.LENGTH_SHORT).show());

        String[] predefinedMatricules = new String[]{
                "ABC-1234", "DEF-5678", "GHI-9012", "JKL-3456", "MNO-7890"
        };

        List<String> matchedMatricules = new ArrayList<>();
        for (Ambulance ambulance : ambulanceList) {
            for (String predefinedMatricule : predefinedMatricules) {
                if (predefinedMatricule.equals(ambulance.getMatricule()) &&
                        "Available".equalsIgnoreCase(ambulance.getDisponibilite())) {
                    matchedMatricules.add(ambulance.getMatricule());
                }
            }
        }

        ArrayAdapter<String> matriculesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, matchedMatricules);
        editTextMatricule.setAdapter(matriculesAdapter);

        editTextMatricule.setOnItemClickListener((adapterView, view, position, id) ->
                Toast.makeText(EditAmbulanceActivity.this,
                        "Selected Matricule: " + editTextMatricule.getText().toString(),
                        Toast.LENGTH_SHORT).show());
    }

    private void loadAmbulanceData(int ambulanceId) {
        Ambulance ambulance = ambulanceService.getAmbulanceById(ambulanceId);
        if (ambulance != null) {
            editTextDriverName.setText(ambulance.getConducteur());
             editTextMatricule.setText(ambulance.getMatricule());
            editTextDestination.setText(ambulance.getDestination());
            editTextDate.setText(ambulance.getDateTransport());
        } else {
            Toast.makeText(this, "Ambulance not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveAmbulanceData() {
        String conducteur = editTextDriverName.getText().toString().trim();
        String matricule = editTextMatricule.getText().toString().trim();
        String destination = editTextDestination.getText().toString().trim();
        String dateTransport = editTextDate.getText().toString().trim();

        if (conducteur.isEmpty() || matricule.isEmpty() || destination.isEmpty() || dateTransport.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            Ambulance ambulance = new Ambulance();
            ambulance.setId(ambulanceId);
            ambulance.setConducteur(conducteur);
            ambulance.setMatricule(matricule);
            ambulance.setDestination(destination);
            ambulance.setDateTransport(dateTransport);

            updateAmbulanceInDatabase(ambulance);
        }
    }

    private void updateAmbulanceInDatabase(Ambulance ambulance) {
        int rowsAffected = ambulanceService.updateAmbulance(ambulance);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Ambulance updated successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditAmbulanceActivity.this, AmbulanceActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error updating ambulance!", Toast.LENGTH_SHORT).show();
        }
    }
}
