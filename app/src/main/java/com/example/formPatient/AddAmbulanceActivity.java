package com.example.formPatient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formPatient.Entity.Ambulance;
import com.example.formPatient.Service.AmbulanceService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAmbulanceActivity extends AppCompatActivity {

    private EditText editTextModel, editTextPlateNumber, editTextDestination, EditTextDate;
    private Button buttonSave;
    private AutoCompleteTextView editTextAmbulanceName, editTextMatricule;
    private AmbulanceService ambulanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ambulance);
        ambulanceService = new AmbulanceService(this);

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

        editTextAmbulanceName = findViewById(R.id.editTextAmbulanceName);
        ArrayAdapter<String> conducteurAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, matchedConducteurs
        );
        editTextAmbulanceName.setAdapter(conducteurAdapter);

        editTextAmbulanceName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(AddAmbulanceActivity.this,
                        "Selected Driver: " + editTextAmbulanceName.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        String[] predefinedMatricules = new String[]{
                "ABC-1234",
                "DEF-5678",
                "GHI-9012",
                "JKL-3456",
                "MNO-7890"
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
        editTextMatricule = findViewById(R.id.editTextMatricule);
        ArrayAdapter<String> matriculesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, matchedMatricules
        );
        editTextMatricule.setAdapter(matriculesAdapter);

        editTextMatricule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(AddAmbulanceActivity.this,
                        "Selected Matricule: " + editTextMatricule.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        editTextDestination = findViewById(R.id.editTextDestination);
        EditTextDate = findViewById(R.id.editTextDate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveAmbulanceToDatabase());
        EditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddAmbulanceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                                EditTextDate.setText(formattedDate);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }
    private void saveAmbulanceToDatabase() {
        String conducteur = editTextAmbulanceName.getText().toString().trim();
        String disponibilite = "Available";
        String localisation = "36.8992365,10.1699763";
        String destination = editTextDestination.getText().toString().trim();
        String dateTransport = EditTextDate.getText().toString().trim();
        String matricule = editTextMatricule.getText().toString().trim();

        if (conducteur.isEmpty() || disponibilite.isEmpty() || destination.isEmpty() || dateTransport.isEmpty()) {
            Toast.makeText(AddAmbulanceActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Ambulance ambulance = new Ambulance(
                        conducteur,
                        disponibilite,
                        localisation,
                        destination,
                        dateTransport,
                        matricule
                );
                long result = ambulanceService.addAmbulance(ambulance);
                if (result != -1) {
                    Toast.makeText(this, "Ambulance added successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddAmbulanceActivity.this, AmbulanceActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Error adding ambulance!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(AddAmbulanceActivity.this, "Invalid driver contact number", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
