package com.example.clinic;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.LabViewHolder> {

    private List<Lab> labList;
    private Context context;
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;

    public LabAdapter(List<Lab> labList, Context context) {
        this.labList = labList;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context); // Pour les opérations de suppression
    }

    @Override
    public LabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lab, parent, false);
        return new LabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LabViewHolder holder, int position) {
        Lab lab = labList.get(position);

        holder.nameTextView.setText(lab.getName());
        holder.descriptionTextView.setText(lab.getDescription());
        holder.dateTextView.setText(lab.getDate());

        holder.buttonDelete.setOnClickListener(v -> {
            boolean isDeleted = databaseHelper.deleteLab(lab.getName());
            if (isDeleted) {
                labList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, labList.size());
            }
        });

        holder.buttonModify.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifyLabActivity.class);
            intent.putExtra("lab", lab);  // Pass the Lab object directly
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return labList.size();
    }

    public static class LabViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, descriptionTextView, dateTextView;
        Button buttonDelete,buttonModify ;

        public LabViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.labNameTextView);
            descriptionTextView = itemView.findViewById(R.id.labDescriptionTextView);
            dateTextView = itemView.findViewById(R.id.labDateTextView);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonModify=itemView.findViewById(R.id.buttonModify);
        }
    }
}
