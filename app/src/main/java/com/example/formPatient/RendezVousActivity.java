package com.example.formPatient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formPatient.Entity.RendezVous;
import com.example.formPatient.Service.RendezVousService;

import java.util.List;

public class RendezVousActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<RendezVous> rendezVousList;
    private RendezVousService rendezVousService;
    private RendezVousAdapter rendezVousAdapter;
    private ImageView buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rendezvous);

        rendezVousService = new RendezVousService(this);

        buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(RendezVousActivity.this, AddRendezVousActivity.class);
            startActivity(intent);
        });
        recyclerView = findViewById(R.id.recyclerViewRendezVous);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAllRendezVous();
    }
    private void loadAllRendezVous() {
        try {
            rendezVousList = rendezVousService.getAllRendezVous();

            if (rendezVousAdapter == null) {
                rendezVousAdapter = new RendezVousAdapter(this, rendezVousList);
                recyclerView.setAdapter(rendezVousAdapter);
            } else {

                rendezVousAdapter.notifyDataSetChanged();
            }

            if (rendezVousList == null || rendezVousList.isEmpty()) {
                Toast.makeText(this, "No Appointments Found. Please add new appointments.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading appointments: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
