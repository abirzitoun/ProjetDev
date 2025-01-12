package com.example.formPatient.Entity;

public class Ambulance {
    private int id;
    private String conducteur;
    private String disponibilite;
    private String localisation;
    private String destination;
    private String dateTransport;
    private String matricule;

    public Ambulance(String conducteur, String disponibilite, String localisation, String destination, String dateTransport, String matricule) {
        this.conducteur = conducteur;
        this.disponibilite = disponibilite;
        this.localisation = localisation;
        this.destination = destination;
        this.dateTransport = dateTransport;
        this.matricule = matricule;
    }

    public Ambulance(String conducteur, String disponibilite, String destination, String dateTransport, String matricule) {
        this.conducteur = conducteur;
        this.disponibilite = disponibilite;
        this.destination = destination;
        this.dateTransport = dateTransport;
        this.matricule = matricule;
    }

    public Ambulance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConducteur() {
        return conducteur;
    }

    public void setConducteur(String conducteur) {
        this.conducteur = conducteur;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateTransport() {
        return dateTransport;
    }

    public void setDateTransport(String dateTransport) {
        this.dateTransport = dateTransport;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}