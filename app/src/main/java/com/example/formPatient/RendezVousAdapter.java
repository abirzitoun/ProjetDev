package com.example.formPatient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.formPatient.Entity.RendezVous;
import com.example.formPatient.Service.RendezVousService;

import java.util.List;

public class RendezVousAdapter extends RecyclerView.Adapter<RendezVousAdapter.RendezVousViewHolder> {
    private Context context;
    private List<RendezVous> rendezVousList;
    private RendezVousService rendezVousService;
    public RendezVousAdapter(Context context, List<RendezVous> rendezVousList) {
        this.context = context;
        this.rendezVousList = rendezVousList;
        this.rendezVousService = new RendezVousService(context);
    }

    @Override
    public RendezVousViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rendezvous_item, parent, false);
        return new RendezVousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RendezVousViewHolder holder, int position) {
        RendezVous rendezVous = rendezVousList.get(position);
        RendezVousService rendezVousService =new RendezVousService(context);
        holder.textViewPatientName.setText("Patient Name: " + rendezVousService.getPatientNameById(rendezVous.getPatientId()));
        holder.textViewSpecialty.setText("Specialty: " + rendezVous.getSpecialty());
        holder.textViewDate.setText("Date: " + rendezVous.getDateRendezVous());
        holder.textViewPayment.setText("Payment: " + rendezVous.getStatus());


        holder.buttonDelete.setOnClickListener(v -> {
            int result = rendezVousService.deleteRendezVous(rendezVous.getId());
            if (result > 0) {
                rendezVousList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Appointment Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete appointment", Toast.LENGTH_SHORT).show();
            }
        });


        holder.buttonEdit.setOnClickListener(v -> {

                Intent intent = new Intent(context, EditRendezVousActivity.class);
                intent.putExtra("RendezVous_ID", rendezVous.getId());
                context.startActivity(intent);
            System.out.println(
                    "Patient ID:#########"  + rendezVous.getPatientId()
            );
        });

        holder.buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddRendezVousActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return rendezVousList.size();
    }

    public static class RendezVousViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPatientName, textViewSpecialty, textViewDate, textViewPayment;
        Button buttonDelete, buttonEdit,buttonAdd;

        public RendezVousViewHolder(View itemView) {
            super(itemView);
            textViewPatientName = itemView.findViewById(R.id.textViewPatientName);
            textViewSpecialty = itemView.findViewById(R.id.textViewSpecialty);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewPayment = itemView.findViewById(R.id.textViewPayment);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}
