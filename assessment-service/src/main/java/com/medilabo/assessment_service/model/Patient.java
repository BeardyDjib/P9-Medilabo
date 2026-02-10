package com.medilabo.assessment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Modèle représentant les informations d'un patient nécessaires à l'évaluation du diabète.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateDeNaissance;
    private String genre;
}