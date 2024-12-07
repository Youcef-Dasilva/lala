package com.aldrin.salam.model;

import java.sql.Date;

public class DemandesAdhesion {
    private int id_demande;
    private String nom;
    private String prenom;
    private String username;
    private Date date_demande;

    public DemandesAdhesion(int id_demande, String nom, String prenom, String username, Date date_demande) {
        this.id_demande = id_demande;
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.date_demande = date_demande;
    }

    // Getters and setters
    public int getId_demande() {
        return id_demande;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate_demande() {
        return date_demande;
    }

    public void setDate_demande(Date date_demande) {
        this.date_demande = date_demande;
    }

    @Override
    public String toString() {
        return "DemandesAdhesion{" +
                "id_demande=" + id_demande +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", username='" + username + '\'' +
                ", date_demande=" + date_demande +
                '}';
    }
}