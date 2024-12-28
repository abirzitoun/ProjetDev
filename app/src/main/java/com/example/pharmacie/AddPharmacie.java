package com.example.pharmacie;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPharmacie extends AppCompatActivity {

    private ImageView profileIv;
    private EditText nomMedicine, dosageMedicine, prixMedicine, validiteMedicine;
    private FloatingActionButton fab;
    private String id,nom,image,dosage, prix, validite,date,time;
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

        //init action bar
        actionBar = getSupportActionBar();

            // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //init view
        profileIv = findViewById(R.id.profileIv);
        nomMedicine = findViewById(R.id.nomMedicine);
        dosageMedicine = findViewById(R.id.dosageMedicine);
        prixMedicine = findViewById(R.id.prixMedicine);
        validiteMedicine = findViewById(R.id.validiteMedicine);
        fab = findViewById(R.id.fab);
        //get intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if(isEditMode){
            //set toolbar title
            actionBar.setTitle("update Medecine");
            //get data from intent
            id = intent.getStringExtra("id");
            image = intent.getStringExtra("image");
            nom = intent.getStringExtra("nom");
            dosage = intent.getStringExtra("dosage");
            prix = intent.getStringExtra("prix");
            validite = intent.getStringExtra("validite");
            date = intent.getStringExtra("date");
            time = intent.getStringExtra("time");
            //set  value in edit text
            nomMedicine.setText(nom);
            dosageMedicine.setText(dosage);
            prixMedicine.setText(prix);
            validiteMedicine.setText(validite);
            imageUri = Uri.parse(intent.getStringExtra("image"));
            if(image.equals("")){
                profileIv.setImageResource(R.drawable.baseline_health_and_safety_24);
            }else {
                profileIv.setImageURI(Uri.parse(image));
            }

        }else {
        //add mode on
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

    private void showImagePickDialog() {
        //option for dialog

        String options[] = {"camera", "MedecineGallery"};
        // Alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setTitle
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //handle item
                if (which == 0) { // start from 0 index
                    //camera selected
                    if (!checkCameraPermission()) {
                        //request camera permission
                        requestCameraPermission();
                    } else {
                        pickFromCamera();

                    }
                } else if (which == 1) {
                    // medecine galery selected
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
        //contentValues for image info
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
        validite = validiteMedicine.getText().toString();
        //get current time to save as added time
        String timeStamp = "" + System.currentTimeMillis();


        // check filed data
        if (!nom.isEmpty() || !dosage.isEmpty() || !prix.isEmpty() || !validite.isEmpty()) {
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
                        ""+validite,
                        ""+date,
                        ""+timeStamp

                ); ;

                Toast.makeText(getApplicationContext(),"Updated Successfully...",Toast.LENGTH_SHORT).show();

            }else {
                //add mode

                long id =  dbHelper.insertData(
                        ""+imageUri,
                        ""+nom,
                        ""+dosage,
                        ""+prix,
                        ""+validite,
                        ""+timeStamp,
                        ""+timeStamp
                ) ;
                // to check insert data successfully, show a toast msg
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
    //Medicine Image taking with user permission and crop functionality
    // first permission from manifest,check,requestPermission
    //by clicking profileIv open dialog to choose image
    //pickImage and Save in ImageUri variable
    //create a class called "constants" for data base and table filed title
    //now insert data in db from addPharmacie
    