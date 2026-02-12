package com.medilabo.patient_service.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entité représentant un patient en base de données.
 */
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;
    private LocalDate dateDeNaissance;
    private String genre;
    private String adresse;
    private String telephone;

    /**
     * Constructeur par défaut.
     */
    public Patient() {
    }

    /**
     * Constructeur complet pour initialiser un patient.
     *
     * @param id              Identifiant unique.
     * @param prenom          Prénom du patient.
     * @param nom             Nom du patient.
     * @param dateDeNaissance Date de naissance.
     * @param genre           Genre (M/F).
     * @param adresse         Adresse postale.
     * @param telephone       Numéro de téléphone.
     */
    public Patient(Long id, String prenom, String nom, LocalDate dateDeNaissance, String genre, String adresse, String telephone) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.dateDeNaissance = dateDeNaissance;
        this.genre = genre;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public LocalDate getDateDeNaissance() { return dateDeNaissance; }
    public void setDateDeNaissance(LocalDate dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}