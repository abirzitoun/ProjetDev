package com.example.clinic;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddLabActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextDate;
    private Button buttonAddLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lab);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.dateadd);
        buttonAddLab = findViewById(R.id.buttonAdd);

        buttonAddLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();

                if (name.isEmpty() || description.isEmpty() || date.isEmpty()) {
                    Toast.makeText(AddLabActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper dbHelper = new DatabaseHelper(AddLabActivity.this);
                    dbHelper.addSample(name, description, date);
                    Toast.makeText(AddLabActivity.this, "Lab added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddLabActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

}

