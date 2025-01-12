package com.example.formPatient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formPatient.Entity.Ambulance;
import com.example.formPatient.Service.AmbulanceService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.AmbulanceViewHolder> {

    private static final double GEOFENCE_LATITUDE = 36.8992365;
    private static final double GEOFENCE_LONGITUDE = 10.1699763;
    private static final float GEOFENCE_RADIUS = 50.0f;
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;
    private List<Ambulance> ambulanceList;
    private List<Ambulance> ambulanceListFull;
    private AmbulanceService ambulanceService;

    public AmbulanceAdapter(Context context, List<Ambulance> ambulanceList) {
        this.context = context;
        this.ambulanceList = ambulanceList;
        this.ambulanceListFull = new ArrayList<>(ambulanceList);
        this.ambulanceService = new AmbulanceService(context);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @NonNull
    @Override
    public AmbulanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ambulance_item, parent, false);
        return new AmbulanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceViewHolder holder, int position) {
        Ambulance ambulance = ambulanceList.get(position);

        holder.textViewName.setText("M : " + ambulance.getMatricule());
        holder.textViewType.setText("Availability: " + ambulance.getDisponibilite());
        holder.textViewConducteur.setText("Driver: " + ambulance.getConducteur());
        holder.textViewLocation.setText("Location: " + ambulance.getLocalisation());
        holder.textViewDestination.setText("Destination: " + ambulance.getDestination());
        holder.textViewDate.setText("Date of Transport: " + ambulance.getDateTransport());


        String availabilityText = ambulance.getDisponibilite();

        // Set the "Availability: " part
        String label = "Availability: ";
        String status = availabilityText != null ? availabilityText : "";

        // Use SpannableString to change color of status part (Available / Unavailable)
        SpannableString spannableString = new SpannableString(label + status);

        // Set "Availability: " part to black
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black)),
                0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the "Available" or "Unavailable" part to green or red
        if (status.contains("Available")) {
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.register_bk_color)),
                    label.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (status.contains("Unavailable")) {
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
                    label.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        holder.textViewType.setText(spannableString);

        holder.buttonDelete.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION && position < ambulanceList.size()) {
                int result = ambulanceService.deleteAmbulance(ambulance.getId());

                if (result > 0) {
                    ambulanceList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, ambulanceList.size());
                    Toast.makeText(context, "Ambulance Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete ambulance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle location click based on the ambulance ID
        if (ambulance.getId() != 1) {
            holder.location.setOnClickListener(v -> {
                String location = ambulance.getLocalisation();
                String[] coordinates = location.split(",");
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
                openGoogleMaps(latitude, longitude);
            });
        } else {
            holder.location.setOnClickListener(v -> {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            openGoogleMaps(latitude, longitude);
                        } else {
                            Toast.makeText(context, "Unable to fetch location. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "Location permission is required.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditAmbulanceActivity.class);
            intent.putExtra("Ambulance_ID", ambulance.getId());
            context.startActivity(intent);
        });

        holder.buttonShow.setOnClickListener(v -> {
            Intent intent = new Intent(context, AmbulanceDetailsActivity.class);
            intent.putExtra("Ambulance_ID", ambulance.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ambulanceList.size();
    }

    public void updateData(List<Ambulance> newAmbulanceList) {
        this.ambulanceList = newAmbulanceList;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            ambulanceList.clear();
            ambulanceList.addAll(ambulanceListFull);
        } else {
            List<Ambulance> filteredList = new ArrayList<>();
            for (Ambulance ambulance : ambulanceListFull) {
                if (ambulance.getConducteur().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(ambulance);
                }
            }
            ambulanceList.clear();
            ambulanceList.addAll(filteredList);
        }
        notifyDataSetChanged();
    }

    public static class AmbulanceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewType, textViewConducteur, textViewLocation, textViewDestination, textViewDate;
        Button buttonDelete, buttonEdit, buttonShow;
        EditText location;

        public AmbulanceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.ambulance_name);
            textViewType = itemView.findViewById(R.id.ambulance_type);
            textViewConducteur = itemView.findViewById(R.id.ambulance_conducteur);
            textViewLocation = itemView.findViewById(R.id.ambulance_location);
            textViewDestination = itemView.findViewById(R.id.ambulance_destination);
            textViewDate = itemView.findViewById(R.id.ambulance_date);
            buttonDelete = itemView.findViewById(R.id.button_delete);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonShow = itemView.findViewById(R.id.button_show);
            location = itemView.findViewById(R.id.location);
        }
    }

    private void openGoogleMaps(double latitude, double longitude) {
        String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=" + GEOFENCE_LATITUDE + "," + GEOFENCE_LONGITUDE + "&destination=" + latitude + "," + longitude + "&travelmode=driving";
        Uri gmmIntentUri = Uri.parse(googleMapsUrl);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
