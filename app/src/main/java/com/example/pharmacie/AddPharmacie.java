package com.example.pharmacie;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.ContentResolver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPharmacie extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private ImageView profileIv;
    private EditText nomMedicine, dosageMedicine,quantityMedecine, prixMedicine, validiteMedicine;
    private FloatingActionButton fab;
    private String id,nom,image,dosage,quantity, prix, validite,date,time;
    private Boolean isEditMode ;
    private ActionBar actionBar;
    //permission constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //string array of permission
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //ImageUri var
    Uri imageUri;

    //database
    private DbHelper dbHelper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pharmacie);
        //init db
        dbHelper = new DbHelper(this);
        //init permission
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    // Get the ambient light level (lux)
                    float lightLevel = event.values[0];

                    // Adjust the screen brightness based on the light level
                    setScreenBrightnessBasedOnLight(lightLevel);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Not needed for this example
            }
        };


        //init action bar
        actionBar = getSupportActionBar();

            // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //init view
        profileIv = findViewById(R.id.profileIv);
        nomMedicine = findViewById(R.id.nomMedicine);
        dosageMedicine = findViewById(R.id.dosageMedicine);
        quantityMedecine = findViewById(R.id.quantityMedecine);
        prixMedicine = findViewById(R.id.prixMedicine);
        validiteMedicine = findViewById(R.id.validiteMedicine);
        fab = findViewById(R.id.fab);
        //récuperer intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if(isEditMode){
            //toolbar title
            actionBar.setTitle("update Medecine");
            //récuperer data de intent
            id = intent.getStringExtra("id");
            image = intent.getStringExtra("image");
            nom = intent.getStringExtra("nom");
            dosage = intent.getStringExtra("dosage");
            prix = intent.getStringExtra("prix");
            quantity = intent.getStringExtra("quantity");
            validite = intent.getStringExtra("validite");
            date = intent.getStringExtra("date");
            time = intent.getStringExtra("time");
            //set valeur in edit text
            nomMedicine.setText(nom);
            dosageMedicine.setText(dosage);
            prixMedicine.setText(prix);
            quantityMedecine.setText(quantity);
            validiteMedicine.setText(validite);
            imageUri = Uri.parse(intent.getStringExtra("image"));
            if(image.equals("")){
                profileIv.setImageResource(R.drawable.baseline_health_and_safety_24);
            }else {
                profileIv.setImageURI(Uri.parse(image));
            }

        }else {
            actionBar.setTitle("Add Medecine");
        }

        // add event handler
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }

        });


    }
    private void setScreenBrightnessBasedOnLight(float lightLevel) {
        int brightness = (int) (lightLevel * 2);
        brightness = Math.max(0, Math.min(brightness, 255));
        ContentResolver contentResolver = getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (lightSensor != null) {
            sensorManager.unregisterListener(lightSensorListener);
        }
    }

    //prendre une photo avec la caméra ou choisir une image dans la galerie de médicaments
    private void showImagePickDialog() {
        String options[] = {"camera", "MedecineGallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();

                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromMedicineGallery();
                    }
                }
            }
        }).create().show();
    }

    private void pickFromMedicineGallery() {
        //intent to pick image from gallery
        Intent MedecineGalleryIntent = new Intent(Intent.ACTION_PICK);
        MedecineGalleryIntent.setType("image/*");
        startActivityForResult(MedecineGalleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Details");
        //save imageUri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private void saveData() {
        // take user giver data in variable and save it in database
        nom = nomMedicine.getText().toString();
        dosage = dosageMedicine.getText().toString();
        prix = prixMedicine.getText().toString();
        quantity = quantityMedecine.getText().toString();
        validite = validiteMedicine.getText().toString();
        //get current time to save as added time
        String timeStamp = "" + System.currentTimeMillis();


        // check filed data
        if (!nom.isEmpty() || !dosage.isEmpty() || !prix.isEmpty() || !validite.isEmpty() || !quantity.isEmpty()) {
            //save data ,if user have only one data
            //check edit or add mode to save data in sql
            if(isEditMode){
                //edit mode
                 dbHelper.updateData(
                         ""+id,
                        ""+imageUri,
                        ""+nom,
                        ""+dosage,
                        ""+prix,
                        ""+quantity,
                        ""+validite,
                        ""+date,
                        ""+timeStamp

                );

                Toast.makeText(getApplicationContext(),"Updated Successfully...",Toast.LENGTH_SHORT).show();

            }else {
                //add mode

                long id =  dbHelper.insertData(
                        ""+imageUri,
                        ""+nom,
                        ""+dosage,
                        ""+prix,
                        ""+quantity,
                        ""+validite,
                        ""+timeStamp,
                        ""+timeStamp
                ) ;
                Toast.makeText(getApplicationContext(),"Inserted Successfully..."+id,Toast.LENGTH_SHORT).show() ;
            }

        } else {
            Toast.makeText(this, "nothing to save..", Toast.LENGTH_SHORT).show();
        }


    }


    //back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //check camera permission
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result & result1;
    }

    //Request camera permission
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_PERMISSION_CODE); // handel request permission on override methode
    }

    //check storage permission
    private boolean checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13 and above
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Android 12 and below
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    //Request storage permission
    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    //handle request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    //if all permission allowed return true , otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                   // boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED ;
                    if (cameraAccepted) {
                        pickFromCamera();
                    } else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Camera permission are required..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    //if all permission allowed return true , otherwise false

                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // permission granted
                        pickFromMedicineGallery();
                    } else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Storage permission are required..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    } 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE && data != null) {
                // Obtenir l'image de la galerie
                imageUri = data.getData();
                profileIv.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // Déjà stocké dans `imageUri` lors de l'ouverture de la caméra
                profileIv.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "Action annulée ou erreur.", Toast.LENGTH_SHORT).show();
        }
    }

}

    