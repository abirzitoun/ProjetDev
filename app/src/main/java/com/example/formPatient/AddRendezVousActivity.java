package com.example.formPatient;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.formPatient.Entity.*;
import com.example.formPatient.Service.*;
import java.util.Calendar;
import java.util.List;

public class AddRendezVousActivity extends AppCompatActivity {
    private AutoCompleteTextView patientDropdown, specialtyDropdown;
    private EditText editAppointmentDate;
    private Button buttonSave;
    private PatientService patientService;
    private RendezVousService rendezVousService;
    private List<Patient> patientList;
    private int selectedPatientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rendezvous);

        // Initialize services and views
        patientService = new PatientService(this);
        rendezVousService = new RendezVousService(this);
        patientDropdown = findViewById(R.id.patientDropdown);
        specialtyDropdown = findViewById(R.id.specialtyDropdown);
        editAppointmentDate = findViewById(R.id.editAppointmentDate);
        buttonSave = findViewById(R.id.buttonSave);


        loadPatients();


        String[] specialties = new String[] {
                "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology", "Psychiatry"
        };
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(this, R.layout.drop_down, specialties);
        specialtyDropdown.setAdapter(specialtyAdapter);


        editAppointmentDate.setOnClickListener(v -> showDatePicker());


        buttonSave.setOnClickListener(v -> {
            String specialty = specialtyDropdown.getText().toString().trim();
            String dateRendezVous = editAppointmentDate.getText().toString().trim();
            String status = "Pending";

            if (specialty.isEmpty() || selectedPatientId == -1 || dateRendezVous.isEmpty()) {
                Toast.makeText(AddRendezVousActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    RendezVous rendezVous = new RendezVous(
                            selectedPatientId,
                            specialty,
                            dateRendezVous,
                            status
                    );
                    saveRendezVous(rendezVous);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddRendezVousActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPatients() {

        patientList = patientService.showAllPatient();
        if (patientList != null && !patientList.isEmpty()) {
            String[] patientNames = new String[patientList.size()];
            for (int i = 0; i < patientList.size(); i++) {
                patientNames[i] = patientList.get(i).getNom() + " " + patientList.get(i).getPrenom();
            }

            ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(this, R.layout.drop_down, patientNames);
            patientDropdown.setAdapter(patientAdapter);


            patientDropdown.setOnItemClickListener((adapterView, view, position, id) -> {
                selectedPatientId = patientList.get(position).getId();
                Toast.makeText(AddRendezVousActivity.this,
                        "Selected: " + patientList.get(position).getNom(),
                        Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No patients found", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                AddRendezVousActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    editAppointmentDate.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveRendezVous(RendezVous rendezVous) {
        long result = rendezVousService.addRendezVous(rendezVous);
        if (result != -1) {
            Toast.makeText(this, "Rendez Vous added successfully!", Toast.LENGTH_SHORT).show();
            showPaymentOptionDialog(rendezVous);
        } else {
            Toast.makeText(this, "Error adding Rendez Vous!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPaymentOptionDialog(RendezVous rendezVous) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Option");
        builder.setMessage("Would you like to pay now or later?");

        builder.setPositiveButton("Pay Now", (dialog, which) -> {
            rendezVous.setStatus("Confirmed");
            rendezVousService.updateRendezVous1(rendezVous);
            Intent intent = new Intent(AddRendezVousActivity.this, PayActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Pay Later", (dialog, which) -> {
            rendezVous.setStatus("Pending");
            rendezVousService.updateRendezVous1(rendezVous);
            Intent intent = new Intent(AddRendezVousActivity.this, RendezVousActivity.class);
            startActivity(intent);
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
