package com.example.formPatient.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class User {

    private int id;
    private String login;
    private String pwd;
    private String prenom;
    private String nom;
    private String email;
    private boolean actif;
    private int valsync;


    public User() {
    }

    public User(int id, String login, String pwd, String name, String prenom, String email, boolean actif, int valsync) {
        this.id = id;
        this.login = login;
        this.pwd = pwd;
        this.prenom = prenom;
        this.nom = name;
        this.email = email;
        this.actif = actif;
        this.valsync = valsync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public int getValsync() {
        return valsync;
    }

    public void setValsync(int valsync) {
        this.valsync = valsync;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", pwd='" + pwd + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", actif=" + actif +
                ", valsync=" + valsync +
                '}';
    }
}
