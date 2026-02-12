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
 * Service métier gérant la logique de calcul du risque de diabète.
 */
@Service
public class AssessmentService {

    private final PatientProxy patientProxy;
    private final NoteProxy noteProxy;

    private static final List<String> DECLENCHEURS = List.of(
            "hémoglobine a1c", "microalbumine", "taille", "poids", "fumeur",
            "anormal", "cholestérol", "vertiges", "rechute", "réaction", "anticorps"
    );

    /**
     * Constructeur injectant les proxies nécessaires à la récupération des données.
     *
     * @param patientProxy Proxy pour les données administratives.
     * @param noteProxy    Proxy pour l'historique des notes.
     */
    public AssessmentService(PatientProxy patientProxy, NoteProxy noteProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
    }

    /**
     * Génère un diagnostic de risque pour un patient spécifique.
     *
     * @param patientId L'identifiant du patient.
     * @return          Le niveau de risque (None, Borderline, In Danger, Early onset).
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
     * Calcule l'âge à partir de la date de naissance.
     *
     * @param dateDeNaissance La date de naissance du patient.
     * @return                L'âge en années.
     */
    private int calculAge(LocalDate dateDeNaissance) {
        if (dateDeNaissance == null) return 0;
        return Period.between(dateDeNaissance, LocalDate.now()).getYears();
    }

    /**
     * Compte le nombre de termes déclencheurs présents dans l'historique des notes.
     *
     * @param notes La liste des notes médicales.
     * @return      Le nombre total de mots-clés uniques identifiés.
     */
    private int compteDeclencheurs(List<Note> notes) {
        String contenuNotes = notes.stream()
                .map(note -> note.getNote() != null ? note.getNote().toLowerCase() : "")
                .collect(Collectors.joining(" "));

        return (int) DECLENCHEURS.stream()
                .filter(contenuNotes::contains)
                .count();
    }

    /**
     * Applique les règles métier pour définir le niveau de risque final.
     *
     * @param age                Âge du patient.
     * @param genre              Genre du patient (M/F).
     * @param nombreDeclencheurs Nombre de symptômes détectés.
     * @return                   Le libellé du niveau de risque.
     */
    private String determineRisk(int age, String genre, int nombreDeclencheurs) {
        if (nombreDeclencheurs == 0) return "None";

        if (age > 30) {
            if (nombreDeclencheurs >= 8) return "Early onset";
            if (nombreDeclencheurs >= 6) return "In Danger";
            if (nombreDeclencheurs >= 2) return "Borderline";
        } else {
            if ("M".equals(genre)) {
                if (nombreDeclencheurs >= 5) return "Early onset";
                if (nombreDeclencheurs >= 3) return "In Danger";
            } else if ("F".equals(genre)) {
                if (nombreDeclencheurs >= 7) return "Early onset";
                if (nombreDeclencheurs >= 4) return "In Danger";
            }
        }
        return "None";
    }
}