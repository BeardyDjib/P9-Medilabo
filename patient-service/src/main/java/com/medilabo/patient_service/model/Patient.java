package com.medilabo.patient_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
@Data // Génère les Getters, Setters, toString, equals, hashCode
@NoArgsConstructor // Génère le constructeur vide (obligatoire pour JPA)
@AllArgsConstructor // Génère le constructeur avec tous les arguments (utilisé par DataInitializer)
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


}