package com.example.formPatient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formPatient.Entity.Patient;
import com.example.formPatient.Entity.RendezVous;
import com.example.formPatient.Service.PatientService;
import com.example.formPatient.Service.RendezVousService;

import java.util.Calendar;
import java.util.List;

public class EditRendezVousActivity extends AppCompatActivity {

    private AutoCompleteTextView patientDropdown, specialtyDropdown;
    private EditText editAppointmentDate;
    private Button buttonSave;
    private PatientService patientService;
    private RendezVousService rendezVousService;
    private List<Patient> patientList;
    private int selectedPatientId = -1;
    private int rendezVousId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rendezvous);

        initializeServices();
        initializeViews();
        setupSpecialtyDropdown();
        setupDatePicker();
        loadPatients();


        rendezVousId = getIntent().getIntExtra("RendezVous_ID", -1);
        if (rendezVousId != -1) {
            loadRendezVousData(rendezVousId);
        }


        buttonSave.setOnClickListener(v -> saveOrUpdateRendezVous());
    }


    private void initializeServices() {
        patientService = new PatientService(this);
        rendezVousService = new RendezVousService(this);
    }


    private void initializeViews() {
        patientDropdown = findViewById(R.id.patientDropdown);
        specialtyDropdown = findViewById(R.id.specialtyDropdown);
        editAppointmentDate = findViewById(R.id.editAppointmentDate);
        buttonSave = findViewById(R.id.buttonSave);
    }


    private void setupSpecialtyDropdown() {
        String[] specialties = {
                "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology", "Psychiatry"
        };
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(
                this, R.layout.drop_down, specialties
        );
        specialtyDropdown.setAdapter(specialtyAdapter);
    }
    private void setupDatePicker() {
        editAppointmentDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        editAppointmentDate.setText(formattedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

     private void loadPatients() {
        patientList = patientService.showAllPatient();

        if (patientList != null && !patientList.isEmpty()) {
            String[] patientNames = new String[patientList.size()];
            for (int i = 0; i < patientList.size(); i++) {
                patientNames[i] = patientList.get(i).getNom() + " " + patientList.get(i).getPrenom();
            }

            ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(
                    this, R.layout.drop_down, patientNames
            );
            patientDropdown.setAdapter(patientAdapter);

            patientDropdown.setOnItemClickListener((adapterView, view, position, id) -> {
                selectedPatientId = patientList.get(position).getId();
                Toast.makeText(
                        this,
                        "Selected: " + patientList.get(position).getNom(),
                        Toast.LENGTH_SHORT
                ).show();
            });
        } else {
            Toast.makeText(this, "No patients found. Please add patients first.", Toast.LENGTH_LONG).show();
        }
    }
    private void loadRendezVousData(int rendezVousId) {
        RendezVous rendezVous = rendezVousService.getRendezVousById(rendezVousId);

        if (rendezVous != null) {
            selectedPatientId = rendezVous.getPatientId();
            editAppointmentDate.setText(rendezVous.getSpecialty());
            specialtyDropdown.setText(rendezVous.getDateRendezVous(), false);

            for (Patient patient : patientList) {
                if (patient.getId() == selectedPatientId) {
                    patientDropdown.setText(patient.getNom() + " " + patient.getPrenom(), false);
                    break;
                }
            }
        } else {
            Toast.makeText(this, "Error loading appointment data", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveOrUpdateRendezVous() {
        String specialty = specialtyDropdown.getText().toString().trim();
        String dateRendezVous = editAppointmentDate.getText().toString().trim();

        if (selectedPatientId == -1 || specialty.isEmpty() || dateRendezVous.isEmpty()) {
            Toast.makeText(this, "Please complete all fields before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        RendezVous rendezVous = new RendezVous(selectedPatientId, specialty, dateRendezVous,  "Pending");
        boolean success;
        if (rendezVousId == -1) {
            success = rendezVousService.addRendezVous(rendezVous) != -1;
        } else {
            rendezVous.setId(rendezVousId);
            success = rendezVousService.updateRendezVous(rendezVous) > 0;
        }
        if (success) {
            Toast.makeText(this, "Appointment saved successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditRendezVousActivity.this, RendezVousActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error saving appointment", Toast.LENGTH_SHORT).show();
        }
    }
}
