package com.example.formPatient.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patients")
public class Patient {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String prenom;
    private Long numTele;
    private String dateNaissance;
    private String dateCreation;
    private String sexe;
    private String sang;
    private String allergie;

    public Patient() {}

    public Patient(int id, String nom, String prenom, Long numTele, String dateNaissance, String dateCreation, String sexe, String sang, String allergie) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numTele = numTele;
        this.dateNaissance = dateNaissance;
        this.dateCreation = dateCreation;
        this.sexe = sexe;
        this.sang = sang;
        this.allergie = allergie;
    }
    public Patient(String nom, String prenom, Long numTele, String dateNaissance, String dateCreation, String sexe, String sang, String allergie) {

        this.nom = nom;
        this.prenom = prenom;
        this.numTele = numTele;
        this.dateNaissance = dateNaissance;
        this.dateCreation = dateCreation;
        this.sexe = sexe;
        this.sang = sang;
        this.allergie = allergie;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Long getNumTele() {
        return numTele;
    }

    public void setNumTele(Long numTele) {
        this.numTele = numTele;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getSang() {
        return sang;
    }

    public void setSang(String sang) {
        this.sang = sang;
    }

    public String getAllergie() {
        return allergie;
    }

    public void setAllergie(String allergie) {
        this.allergie = allergie;
    }
}
