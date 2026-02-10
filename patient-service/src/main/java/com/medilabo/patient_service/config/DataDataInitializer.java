package com.medilabo.patient_service.config;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Classe de configuration responsable de l'initialisation des données de test
 * dans la base de données au démarrage de l'application.
 * <p>
 * Cette classe permet de pré-charger les jeux de données nécessaires
 * aux tests d'intégration et aux scénarios d'évaluation du risque.
 * </p>
 */
@Configuration
public class DataDataInitializer {

    /**
     * Initialise la base de données avec les patients de test si celle-ci est vide.
     * <p>
     * Ce bean s'exécute au démarrage de l'application et insère les quatre profils
     * de patients requis pour valider les règles métier (None, Borderline, In Danger, Early Onset).
     * L'identifiant est passé à {@code null} pour déléguer la génération de la clé primaire
     * à la base de données (stratégie AUTO_INCREMENT).
     * </p>
     *
     * @param repository Le repository permettant la persistance des entités Patient.
     * @return Une instance de {@link CommandLineRunner} exécutée par Spring Boot.
     */
    @Bean
    CommandLineRunner initDatabase(PatientRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Insertion du Patient 1 : Cas de test "None" (Aucun risque)
                repository.save(new Patient(null, "TestNone", "Test", LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333"));

                // Insertion du Patient 2 : Cas de test "Borderline" (Risque limité)
                repository.save(new Patient(null, "TestBorderline", "Test", LocalDate.of(1945, 6, 24), "M", "2 High St", "200-333-4444"));

                // Insertion du Patient 3 : Cas de test "In Danger" (Danger)
                repository.save(new Patient(null, "TestInDanger", "Test", LocalDate.of(2004, 6, 18), "M", "3 Club Road", "300-444-5555"));

                // Insertion du Patient 4 : Cas de test "Early Onset" (Apparition précoce)
                repository.save(new Patient(null, "TestEarlyOnset", "Test", LocalDate.of(2002, 6, 28), "F", "4 Valley Dr", "400-555-6666"));

                System.out.println("INFO: Données de test initialisées avec succès.");
            }
        };
    }
}