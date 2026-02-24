package com.medilabo.patient_service.repository;

import com.medilabo.patient_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface Repository pour l'accès aux données Patient.
 * <p>
 * Utilise Spring Data JPA pour la persistance dans une base relationnelle (SQL).
 * L'identifiant de l'entité est de type {@link Long} pour correspondre à la stratégie
 * d'auto-incrémentation numérique gérée nativement par la base de données.
 * </p>
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}