package com.example.pharmacie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;

import java.util.ArrayList;

// Adapter class pour afficher des données dans un RecyclerView
public class AdapterPharmacie extends RecyclerView.Adapter<AdapterPharmacie.PharmacieViewHolder> {
    private Context context;
    private ArrayList<ModelPharmacie> pharmacieArrayList;
    private DbHelper dbHelper;

    public AdapterPharmacie(ArrayList<ModelPharmacie> pharmacieArrayList, Context context) {
        this.pharmacieArrayList = pharmacieArrayList;
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public PharmacieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pharmacie_item, parent, false);
        return new PharmacieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacieViewHolder holder, int position) {
        ModelPharmacie modelPharmacie = pharmacieArrayList.get(position);

        // Récupérer les données
        String id = modelPharmacie.getId();
        String image = modelPharmacie.getImage();
        String nom = modelPharmacie.getNom();
        String dosage = modelPharmacie.getDosage();
        String prix = modelPharmacie.getPrix();
        String validite = modelPharmacie.getValidite();
        String date = modelPharmacie.getDate();
        String time = modelPharmacie.getTime();

        // Définir les données dans les vues
        holder.medicineName.setText(nom);

        if (image == null || image.isEmpty()) {
            holder.medicineImage.setImageResource(R.drawable.baseline_health_and_safety_24);
        } else {
            try {
                holder.medicineImage.setImageURI(Uri.parse(image));
            } catch (Exception e) {
                holder.medicineImage.setImageResource(R.drawable.baseline_health_and_safety_24);
            }
        }

        // Gestion des clics

        //handle item click and show pharmacie details
        holder.relativeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // create intent to move to pharmacie details activity with pharmacie id  as reference
                Intent intent = new Intent(context, PharmacieDetails.class);
                intent.putExtra("pharmacieId", id);
                context.startActivity(intent);
                Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();} // Get data from details activity

        });
        //handle editbtn click
        holder.pharmacieEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent to move AddActivity to update dta
                Intent intent = new Intent(context, AddPharmacie.class);
                // pass the value of cuurent position
                intent.putExtra("id", id);
                intent.putExtra("image", image);
                intent.putExtra("nom", nom);
                intent.putExtra("dosage", dosage);
                intent.putExtra("prix", prix);
                intent.putExtra("validite", validite);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                // pass a boolean data to know that we are in edit mode
                intent.putExtra("isEditMode", true);
                // start intent
                context.startActivity(intent);
            }
        });
        //handle deletebtn click
        holder.PharmacieDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dbHelper.deleteData(id);
                // refresh data
                ((MainActivity)context).onResume();
            }
        });


    }

    @Override
    public int getItemCount() {
        return pharmacieArrayList.size();
    }

    static class PharmacieViewHolder extends RecyclerView.ViewHolder {
        // Vues pour row_pharmacie_item
        ImageView medicineImage, dosageButton;
        TextView medicineName,pharmacieEdit,PharmacieDelete;
         RelativeLayout relativeLayout ;

        public PharmacieViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialisation des vues
            medicineImage = itemView.findViewById(R.id.medicine_image);
            medicineName = itemView.findViewById(R.id.medicine_name);
            dosageButton = itemView.findViewById(R.id.dosageMedicine);
            pharmacieEdit = itemView.findViewById(R.id.pharmacie_edit);
            PharmacieDelete = itemView.findViewById(R.id.pharmacie_delete);
            relativeLayout = itemView.findViewById(R.id.mainLayout);

        }
    }

}
