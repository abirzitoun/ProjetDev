package com.example.formPatient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formPatient.Entity.Patient;
import com.example.formPatient.Service.PatientService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditPatientActivity extends AppCompatActivity {

    private EditText editTextFName, editTextLName, editTextPhone, editBirthDate;
    private Button buttonSave;
    private AutoCompleteTextView bloodType, allergiess;
    private RadioButton radioMale;
    private int patientId;
    private PatientService servicePatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        editTextFName = findViewById(R.id.editTextFName);
        editTextLName = findViewById(R.id.editTextLName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editBirthDate = findViewById(R.id.editBirthDate);
        bloodType = findViewById(R.id.bloodType);
        allergiess = findViewById(R.id.allergies);
        radioMale = findViewById(R.id.radioMale);
        buttonSave = findViewById(R.id.buttonSave);


        servicePatient = new PatientService(this);

        setupDropdownMenus();

        Intent intent = getIntent();
        patientId = intent.getIntExtra("Patient_ID", -1);

        if (patientId != -1) {
            loadPatientData(patientId);
        }


        buttonSave.setOnClickListener(v -> savePatientChanges());
    }

    private void setupDropdownMenus() {
        // Blood type options
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, R.layout.drop_down, bloodTypes);
        bloodType.setAdapter(bloodTypeAdapter);

        // Allergies options
        String[] allergies = {
                "Peanuts", "Shellfish", "Dairy", "Eggs", "Wheat", "Soy", "Latex",
                "Dust", "Pollen", "Penicillin", "Mold", "Bee Stings", "Aspirin",
                "Nickel", "Chocolates", "Fish", "Tree Nuts", "Sesame", "Garlic"
        };
        ArrayAdapter<String> allergiesAdapter = new ArrayAdapter<>(this, R.layout.drop_down, allergies);
        allergiess.setAdapter(allergiesAdapter);
    }

    private void loadPatientData(int patientId) {
        Patient patient = servicePatient.getPatientById(patientId);
        if (patient != null) {
            editTextFName.setText(patient.getNom());
            editTextLName.setText(patient.getPrenom());
            editTextPhone.setText(String.valueOf(patient.getNumTele()));
            editBirthDate.setText(patient.getDateNaissance());
            bloodType.setText(patient.getSang(), false);
            allergiess.setText(patient.getAllergie(), false);
            radioMale.setChecked("Male".equals(patient.getSexe()));
        } else {
            Toast.makeText(this, "Failed to load patient data", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePatientChanges() {
        String nom = editTextFName.getText().toString().trim();
        String prenom = editTextLName.getText().toString().trim();
        String phoneStr = editTextPhone.getText().toString().trim();
        String dateNaissance = editBirthDate.getText().toString().trim();
        String sang = bloodType.getText().toString().trim();
        String allergie = allergiess.getText().toString().trim();
        String sexe = radioMale.isChecked() ? "Male" : "Female";

        if (nom.isEmpty() || prenom.isEmpty() || phoneStr.isEmpty() || dateNaissance.isEmpty() || sang.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Long phone = Long.parseLong(phoneStr);

            Patient patient = new Patient(nom, prenom, phone, dateNaissance, null, sexe, sang, allergie);
            patient.setId(patientId);

            int rowsAffected = servicePatient.updatePatient(patient);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Patient updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditPatientActivity.this, PatientActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to update patient!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
        }
    }
}
