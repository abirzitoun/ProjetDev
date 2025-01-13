package com.example.formPatient.Service;


import androidx.room.Dao;

import com.example.formPatient.Entity.Patient;

import java.util.List;

@Dao
public interface IPatientDao {
    public List<Patient> showAllPatient();



    long addPatient(Patient patient);

    int updatePatient(Patient patient);

    public int deletePatient(int commentId);

}