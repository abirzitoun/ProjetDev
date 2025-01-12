package com.example.formPatient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.formPatient.Entity.Patient;
import com.example.formPatient.Service.PatientService;
public class PatientDetailsActivity extends AppCompatActivity {

    private TextView textNom, textDateNaissance,textSexe,
    textAllergie, textDaeRenderVous ,textSpacialite,numTele ,textPaiement;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_detail);

        textNom = findViewById(R.id.text_name);
        textDateNaissance = findViewById(R.id.text_birth_date);
        numTele = findViewById(R.id.text_phone);
         textSexe = findViewById(R.id.text_gender);
         textAllergie = findViewById(R.id.text_allergies);


         int patientId = getIntent().getIntExtra("Patient_ID", -1);

        if (patientId != -1) {

            PatientService patientService = new PatientService(this);
            Patient patient = patientService.getPatientById(patientId);

            if (patient != null) {
                 textNom.setText("Name: " + patient.getNom() + " " + patient.getPrenom());
                textDateNaissance.setText("DateNaissance: " + patient.getDateNaissance());
                numTele.setText("NumTele: " + patient.getNumTele());
                textSexe.setText("Sexe: " + patient.getSexe());
                textAllergie.setText("Allergie: " + patient.getAllergie());
               // textDaeRenderVous.setText("DaeRenderVous: " + patient.getDaeRenderVous());
               // textSpacialite.setText("Spacialite: " + patient.getSpacialite());

            } else {
                textNom.setText("Patient not found");
            }
        } else {
            textNom.setText("Invalid Patient ID");
        }
    }
}
