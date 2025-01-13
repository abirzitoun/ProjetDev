package com.example.clinic;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyLabActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, dateEditText;
    private Button saveButton,buttonModify;
    private Lab labToModify;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_lab);

        // Initialize the views
        nameEditText = findViewById(R.id.nameModify);
        descriptionEditText = findViewById(R.id.descriptionModify);
        dateEditText = findViewById(R.id.dateModify);
        saveButton = findViewById(R.id.modifyButtonSave);

        // Retrieve the lab object passed from the adapter
        labToModify = (Lab) getIntent().getSerializableExtra("lab");

        // Pre-fill the fields with the lab's existing data
        nameEditText.setText(labToModify.getName());
        descriptionEditText.setText(labToModify.getDescription());
        dateEditText.setText(labToModify.getDate());

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Action when the "Save" button is clicked
        saveButton.setOnClickListener(v -> {
            // Update the lab object with the new data
            labToModify.setName(nameEditText.getText().toString());
            labToModify.setDescription(descriptionEditText.getText().toString());
            labToModify.setDate(dateEditText.getText().toString());

            // Save the updated lab data in the database
            databaseHelper.updateLab(labToModify);

            // Return to the previous activity
            finish();
        });
    }

}

