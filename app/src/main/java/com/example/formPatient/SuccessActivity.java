package com.example.formPatient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

         String tagData = getIntent().getStringExtra("tagData");

         TextView successMessage = findViewById(R.id.tv_success_message);
        successMessage.setText("Payment Successful for: " + tagData);

        ImageView logoImage = findViewById(R.id.logoImage);
        logoImage.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessActivity.this, RendezVousActivity.class);
            startActivity(intent);
        });
    }
}
