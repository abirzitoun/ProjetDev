package com.example.formPatient;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.formPatient.Entity.Patient;
import com.example.formPatient.Service.PatientService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity {
    private EditText editTextFName, editTextLName, editTextPhone, editBirthDate ;
    private Button buttonSave;
    private EditText editAppointmentDate;
    private AutoCompleteTextView specialty,bloodType,allergiess;
    private RadioButton radioMale;
    private MyDatabaseHelper database;
    private PatientService servicePatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_add);
        String[] type = new String[] {"A+" , "A-", "B+" , "B-" , "AB+" , "AB-" , "O+" , "O-"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this ,R.layout.drop_down , type);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.bloodType);
        autoCompleteTextView.setAdapter(adapter3);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView3, View view, int position, long id) {
                Toast.makeText(AddPatientActivity.this , autoCompleteTextView.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });

        String[] allergies = new String[] {
                "Peanuts", "Shellfish", "Dairy", "Eggs", "Wheat", "Soy", "Latex",
                "Dust", "Pollen", "Penicillin", "Mold", "Bee Stings", "Aspirin",
                "Nickel", "Chocolates", "Fish", "Tree Nuts", "Sesame", "Garlic"
        };
        ArrayAdapter<String> allergiesAdapter = new ArrayAdapter<>(this, R.layout.drop_down, allergies);

        AutoCompleteTextView allergiesTextView = findViewById(R.id.allergies);
        allergiesTextView.setAdapter(allergiesAdapter);
        allergiesTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(AddPatientActivity.this, "Selected Allergy: " + allergiesTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        String[] specialties = new String[] {
                "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology", "Psychiatry",
                "Gynecology", "General Surgery", "Endocrinology", "Rheumatology", "Gastroenterology",
                "Oncology", "Pulmonology", "Urology", "Ophthalmology", "ENT", "Pathology", "Infectious Disease",
                "Plastic Surgery", "Nephrology"
        };
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(this, R.layout.drop_down, specialties);

        editTextFName = findViewById(R.id.editTextFName);
        editTextLName = findViewById(R.id.editTextLName);
        editTextPhone = findViewById(R.id.editTextPhone);
        bloodType = findViewById(R.id.bloodType);
        allergiess = findViewById(R.id.allergies);
        editBirthDate = findViewById(R.id.editBirthDate);
        radioMale = findViewById(R.id.radioMale);
        buttonSave = findViewById(R.id.buttonSave);
        database  = new MyDatabaseHelper(this);

        servicePatient = new PatientService(this);

        EditText editBirthDate = findViewById(R.id.editBirthDate);



        editBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPatientActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                                editBirthDate.setText(formattedDate);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
        buttonSave.setOnClickListener(v -> {
            String nom = editTextFName.getText().toString().trim();
            String prenom = editTextLName.getText().toString().trim();
            String numTeleStr = editTextPhone.getText().toString().trim();
            String dateNaissance = editBirthDate.getText().toString().trim();
            String dateCreation = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String sexe = radioMale.isChecked() ? "Male" : "Female";
            String sang = bloodType.getText().toString().trim();
            String allergie = allergiess.getText().toString().trim();

            if (nom.isEmpty() || prenom.isEmpty() || numTeleStr.isEmpty() || dateNaissance.isEmpty() || sang.isEmpty()) {
                Toast.makeText(AddPatientActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Long numTele = Long.parseLong(numTeleStr);

                    Patient patient = new Patient(
                            nom,
                            prenom,
                            numTele,
                            dateNaissance,
                            dateCreation,
                            sexe,
                            sang,
                            allergie
                    );

                     savePatientToDatabase(patient);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddPatientActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void savePatientToDatabase(Patient patient) {
         long result = servicePatient.addPatient(patient);

        if (result != -1) {
            Toast.makeText(this, "Patient added successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AddPatientActivity.this, PatientActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error adding patient!", Toast.LENGTH_SHORT).show();
        }
    }
}
