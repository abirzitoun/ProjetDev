package com.example.formPatient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formPatient.Entity.Ambulance;
import com.example.formPatient.Service.AmbulanceService;
import com.example.formPatient.Service.PatientService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;

import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class AmbulanceActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final double GEOFENCE_LATITUDE = 36.8992365;
    private static final double GEOFENCE_LONGITUDE = 10.1699763;
    private static final float GEOFENCE_RADIUS = 50.0f;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private List<Ambulance> ambulanceList;
    private AmbulanceService ambulanceService;
    private AmbulanceAdapter ambulanceAdapter;
    private ImageView buttonAdd;
    private AmbulanceService serviceAmbulance;
    private EditText buttonShowLocation;
    private TextInputEditText searchViewConducteur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);
        dbHelper = new MyDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationUpdates();
        serviceAmbulance = new AmbulanceService(this);

        EditText btnGoToMyLocation = findViewById(R.id.button_show_location);
        btnGoToMyLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        openGoogleMaps(latitude, longitude);
                    } else {
                        Toast.makeText(this, "Unable to fetch location. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show();
            }
        });

        initViews();
        setupRecyclerView();
        setupListeners();
        loadAllAmbulances();
    }
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewAmbulances);
        buttonAdd = findViewById(R.id.button_add);
       // buttonShowLocation = findViewById(R.id.button_show_location);
        searchViewConducteur = findViewById(R.id.search_view_conducteur);
        ambulanceService = new AmbulanceService(this);
    }
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AmbulanceActivity.this, AddAmbulanceActivity.class);
            startActivity(intent);
        });

      //  buttonShowLocation.setOnClickListener(v -> openLocationInMaps());

        searchViewConducteur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ambulanceAdapter != null) {
                    ambulanceAdapter.filter(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void loadAllAmbulances() {
        ambulanceList = ambulanceService.getAllAmbulances();
        if (ambulanceList != null && !ambulanceList.isEmpty()) {
            ambulanceAdapter = new AmbulanceAdapter(this, ambulanceList);
            recyclerView.setAdapter(ambulanceAdapter);
        } else {
            Toast.makeText(this, "No ambulances found", Toast.LENGTH_SHORT).show();
        }
    }
    private void openLocationInMaps() {
        String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=36.8083,10.0974&destination=36.8056,10.0990&travelmode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsUrl));
        startActivity(intent);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No application available to handle map requests!", Toast.LENGTH_SHORT).show();
            Log.e("MapIntent", "No application can handle the map intent.");
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("LocationUpdate", "Current Location: " + location);


                        if (isWithinGeofence(location)) {
                            Toast.makeText(AmbulanceActivity.this, "Ambulance is within the geofence.", Toast.LENGTH_SHORT).show();
                            String newDisponibilite = "Available";
                            int ambulanceId = 1;


                            int result = serviceAmbulance.updateAmbulanceLocationAndDisponibilite(ambulanceId, location, newDisponibilite);
                            if (result > 0) {
                                Log.d("AmbulanceUpdate", "Location and availability updated successfully.");
                            } else {
                                Log.e("AmbulanceUpdate", "Failed to update location and availability.");
                            }
                        } else {

                            Toast.makeText(AmbulanceActivity.this, "ALERT: Ambulance has left the geofence!", Toast.LENGTH_SHORT).show();

                            String newDisponibilite = "Unavailable";
                            int ambulanceId = 1;


                            int result = serviceAmbulance.updateAmbulanceLocationAndDisponibilite(ambulanceId, location, newDisponibilite);

                            if (result > 0) {
                                Log.d("AmbulanceUpdate", "Location and availability updated successfully.");
                            } else {
                                Log.e("AmbulanceUpdate", "Failed to update location and availability.");
                            }
                        }
                    } else {
                        Log.e("LocationUpdate", "Received null location.");
                    }
                }

            }


        };
        EditText reload = findViewById(R.id.reload);
        reload.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                Toast.makeText(this, "Location updates requested!", Toast.LENGTH_SHORT).show();
                refreshPage();
            }
            return false;
        });


    }
    private boolean isWithinGeofence(Location currentLocation) {
        Location geofenceCenter = new Location("GeofenceCenter");
        geofenceCenter.setLatitude(GEOFENCE_LATITUDE);
        geofenceCenter.setLongitude(GEOFENCE_LONGITUDE);

        float distance = currentLocation.distanceTo(geofenceCenter);
        return distance <= GEOFENCE_RADIUS;
    }

    private void openGoogleMaps(double latitude, double longitude) {
        String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=" + GEOFENCE_LATITUDE + "," + GEOFENCE_LONGITUDE + "&destination=" + latitude + "," + longitude + "&travelmode=driving";
        Uri gmmIntentUri = Uri.parse(googleMapsUrl);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        System.out.println("geo:" + latitude + "," + longitude);

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission is required for this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void refreshPage() {

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllAmbulances();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ambulanceService.closeDatabase();
    }
}
