package com.example.pharmacie;
//ModelClass for data
public class ModelPharmacie {
    private String id,nom,image,dosage,prix,validite,date,time;
    //create constructors

    public ModelPharmacie(String id, String nom, String image, String dosage, String prix, String validite, String date, String time) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.dosage = dosage;
        this.prix = prix;
        this.validite = validite;
        this.date = date;
        this.time = time;
    }

    // create getter and setter method

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getValidite() {
        return validite;
    }

    public void setValidite(String validite) {
        this.validite = validite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
