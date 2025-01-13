package com.example.formPatient;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formPatient.Entity.Patient;
import com.example.formPatient.Service.PatientService;


import java.util.List;
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private Context context;
    private List<Patient> patientList;
    private PatientService patientService;
    public PatientAdapter(Context context, List<Patient> PatientList) {
        this.context = context;
        this.patientList = PatientList;
        this.patientService = new PatientService(context);
    }


     @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.patient_item, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        PatientService PatientService = new PatientService();
        Patient patient = patientList.get(position);
        holder.NomTextView.setText(patient.getNom());
        holder.EmailTextView.setText(patient.getDateNaissance());


        holder.buttonShow.setOnClickListener(v -> {
            Intent intent = new Intent(context, PatientDetailsActivity.class);
            intent.putExtra("Patient_ID", patient.getId());
            context.startActivity(intent);
        });


         holder.buttonDelete.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION && position < patientList.size()) {
                Patient patientToDelete = patientList.get(position);

                int result = patientService.deletePatient(patientToDelete.getId());

                if (result > 0) {
                    patientList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, patientList.size());

                    Toast.makeText(context, "Patient Deleted!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete patient", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Invalid position", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonEdit.setOnClickListener(v -> {

            Intent intent = new Intent(context, EditPatientActivity.class);
            intent.putExtra("Patient_ID", patient.getId());
            context.startActivity(intent);
        });
    }


     @Override
    public int getItemCount() {
        return patientList.size();
    }

    public void updateData(List<Patient> newPatients) {
        this.patientList.clear();
        this.patientList.addAll(newPatients);
        notifyDataSetChanged();
    }



    public class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView NomTextView;
        TextView EmailTextView;
        ImageView buttonShow;
        Button buttonDelete,buttonEdit;
        public PatientViewHolder(View itemView) {
            super(itemView);
            NomTextView = itemView.findViewById(R.id.patient_full_name);
            EmailTextView = itemView.findViewById(R.id.patient_email);
            buttonShow = itemView.findViewById(R.id.button_show);
            buttonDelete = itemView.findViewById(R.id.button_delete);
            buttonEdit = itemView.findViewById(R.id.button_edit);
         }
    }
}
