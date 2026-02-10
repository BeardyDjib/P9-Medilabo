package com.medilabo.assessment_service.service;

import com.medilabo.assessment_service.model.Note;
import com.medilabo.assessment_service.model.Patient;
import com.medilabo.assessment_service.proxies.NoteProxy;
import com.medilabo.assessment_service.proxies.PatientProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier chargé de la logique d'évaluation du risque de diabète.
 * Ce service agrège les données des microservices distants (Patient et Note)
 * pour produire un rapport de risque basé sur des règles prédéfinies.
 */
@Service
public class AssessmentService {

    private final PatientProxy patientProxy;
    private final NoteProxy noteProxy;

    private static final List<String> DECLENCHEURS = List.of(
            "hémoglobine a1c",
            "microalbumine",
            "taille",
            "poids",
            "fumeur",
            "anormal",
            "cholestérol",
            "vertiges",
            "rechute",
            "réaction",
            "anticorps"
    );

    public AssessmentService(PatientProxy patientProxy, NoteProxy noteProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
    }

    /**
     * Génère un rapport de risque pour un patient donné.
     * @param patientId Identifiant unique du patient
     * @return Le niveau de risque calculé (None, Borderline, In Danger, Early onset)
     */
    public String generateAssessment(Long patientId) {
        Patient patient = patientProxy.getPatientById(patientId);
        List<Note> notes = noteProxy.getNotesByPatientId(patientId);

        int age = calculAge(patient.getDateDeNaissance());
        int nombreDeclencheurs = compteDeclencheurs(notes);
        String genre = patient.getGenre();

        return determineRisk(age, genre, nombreDeclencheurs);
    }

    /**
     * Calcule l'âge d'un patient à partir de sa date de naissance.
     * @param dateDeNaissance Date de naissance du patient
     * @return Âge en années
     */
    private int calculAge(LocalDate dateDeNaissance) {
        if (dateDeNaissance == null) {
            return 0;
        }
        return Period.between(dateDeNaissance, LocalDate.now()).getYears();
    }

    /**
     * Analyse les notes médicales pour compter le nombre de termes déclencheurs uniques.
     * La recherche est insensible à la casse.
     * @param notes Liste des notes du patient
     * @return Nombre de termes déclencheurs trouvés
     */
    private int compteDeclencheurs(List<Note> notes) {
        int compteur = 0;

        // Concaténation de toutes les notes en une seule chaîne minuscule pour l'analyse
        String contenuNotes = notes.stream()
                .map(note -> note.getNote() != null ? note.getNote().toLowerCase() : "")
                .collect(Collectors.joining(" "));

        for (String declencheur : DECLENCHEURS) {
            if (contenuNotes.contains(declencheur)) {
                compteur++;
            }
        }
        return compteur;
    }

    /**
     * Applique les règles métier pour déterminer le niveau de risque.
     * @param age Âge du patient
     * @param genre Genre du patient (M/F)
     * @param nombreDeclencheurs Nombre de symptômes identifiés
     * @return Le libellé du risque
     */
    private String determineRisk(int age, String genre, int nombreDeclencheurs) {
        if (nombreDeclencheurs == 0) {
            return "None";
        }

        if (age > 30) {
            if (nombreDeclencheurs >= 8) {
                return "Early onset";
            } else if (nombreDeclencheurs >= 6) {
                return "In Danger";
            } else if (nombreDeclencheurs >= 2) {
                return "Borderline";
            }
        } else {
            // Logique pour les moins de 30 ans
            if ("M".equals(genre)) {
                if (nombreDeclencheurs >= 5) {
                    return "Early onset";
                } else if (nombreDeclencheurs >= 3) {
                    return "In Danger";
                }
            } else if ("F".equals(genre)) {
                if (nombreDeclencheurs >= 7) {
                    return "Early onset";
                } else if (nombreDeclencheurs >= 4) {
                    return "In Danger";
                }
            }
        }

        return "None";
    }
}