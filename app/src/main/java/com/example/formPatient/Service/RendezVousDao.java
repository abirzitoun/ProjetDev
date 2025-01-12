package com.example.formPatient.Service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.formPatient.Entity.RendezVous;
import com.example.formPatient.Entity.Patient;

import java.util.List;

@Dao
public interface RendezVousDao {
    @Insert
    void insertRendezVous(RendezVous rendezVous);

    @Query("SELECT * FROM rendezvous WHERE patientId = :patientId")
    List<RendezVous> getRendezVousForPatient(int patientId);

    // Corrected query to reference the correct table "patient" (not "patients")
    @Query("SELECT nom FROM patients WHERE id = :patientId")
    String getPatientNameById(int patientId);
}
