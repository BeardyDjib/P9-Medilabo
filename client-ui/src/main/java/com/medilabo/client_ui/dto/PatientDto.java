package com.medilabo.client_ui.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class PatientDto {

    private Long id;
    private String prenom;
    private String nom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDeNaissance;

    private String genre;
    private String adresse;
    private String telephone;

    public PatientDto() {
    }

    public PatientDto(Long id, String prenom, String nom, LocalDate dateDeNaissance, String genre, String adresse, String telephone) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.dateDeNaissance = dateDeNaissance;
        this.genre = genre;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}