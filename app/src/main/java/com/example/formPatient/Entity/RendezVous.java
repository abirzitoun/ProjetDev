package com.example.formPatient.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import com.example.formPatient.Service.RendezVousDao;
@Entity(
        tableName = "rendezvous",
        foreignKeys = @ForeignKey(
                entity = Patient.class,
                parentColumns = "id",
                childColumns = "patientId",
                onDelete = ForeignKey.CASCADE
        )
)
public class RendezVous {
    private int id;
    private int patientId;
    private String dateRendezVous;
    private String specialty;
    private String status;


    public RendezVous(int patientId, String specialty,String dateRendezVous,  String status) {
        this.patientId = patientId;
        this.dateRendezVous = dateRendezVous;
        this.specialty = specialty;
        this.status = status;
    }

     public RendezVous(int id, int patientId,String specialty, String dateRendezVous,  String status) {
        this.id = id;
        this.patientId = patientId;
         this.specialty = specialty;
        this.dateRendezVous = dateRendezVous;

        this.status = status;
    }

     public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getDateRendezVous() { return dateRendezVous; }
    public void setDateRendezVous(String dateRendezVous) { this.dateRendezVous = dateRendezVous; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


}