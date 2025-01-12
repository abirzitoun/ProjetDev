package com.example.formPatient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.formPatient.Entity.Ambulance;
import com.example.formPatient.Service.AmbulanceService;

public class AmbulanceDetailsActivity extends AppCompatActivity {

    private TextView textConducteur, textDisponibilite, textLocalisation, textDestination, textDateTransport;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambulance_detail);

         textConducteur = findViewById(R.id.text_conducteur);
        textDisponibilite = findViewById(R.id.text_disponibilite);
        textLocalisation = findViewById(R.id.text_localisation);
        textDestination = findViewById(R.id.text_destination);
        textDateTransport = findViewById(R.id.text_date_transport);

         int ambulanceId = getIntent().getIntExtra("Ambulance_ID", -1);

        if (ambulanceId != -1) {
             AmbulanceService ambulanceService = new AmbulanceService(this);
            Ambulance ambulance = ambulanceService.getAmbulanceById(ambulanceId);

             if (ambulance != null) {
                textConducteur.setText("Conducteur: " + ambulance.getConducteur());
                textDisponibilite.setText("Disponibilité: " + ambulance.getDisponibilite());
                textLocalisation.setText("Localisation: " + ambulance.getLocalisation());
                textDestination.setText("Destination: " + ambulance.getDestination());
                textDateTransport.setText("Date de Transport: " + ambulance.getDateTransport());
            } else {
                textConducteur.setText("Ambulance not found");
            }
        } else {
            textConducteur.setText("Invalid Ambulance ID");
        }
    }
}
