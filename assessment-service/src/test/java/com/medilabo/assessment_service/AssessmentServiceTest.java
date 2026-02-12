package com.medilabo.assessment_service.service;

import com.medilabo.assessment_service.model.Note;
import com.medilabo.assessment_service.model.Patient;
import com.medilabo.assessment_service.proxies.NoteProxy;
import com.medilabo.assessment_service.proxies.PatientProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Classe de tests unitaires pour le service d'évaluation des risques (AssessmentService).
 * <p>
 * Ces tests vérifient la logique métier de calcul du risque de diabète
 * en isolant le service via des mocks (simulations) des dépendances externes (Proxies).
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @Mock
    private PatientProxy patientProxy;

    @Mock
    private NoteProxy noteProxy;

    @InjectMocks
    private AssessmentService assessmentService;

    /**
     * Teste le cas où le patient ne présente aucun risque ("None").
     * Conditions : Patient de plus de 30 ans avec moins de 2 déclencheurs.
     */
    @Test
    void generateAssessment_shouldReturnNone_whenNoRisk() {
        // ARRANGE (Préparation des données simulées)
        Long patientId = 1L;
        // Patient de 50 ans (né il y a longtemps)
        Patient mockPatient = new Patient(patientId, "Test", "None", LocalDate.of(1970, 1, 1), "F");
        // Une note sans mot-clé déclencheur
        List<Note> mockNotes = Collections.singletonList(new Note("id1", "Le patient va bien."));

        // On apprend au Mock comment réagir
        when(patientProxy.getPatientById(patientId)).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(patientId)).thenReturn(mockNotes);

        // ACT (Exécution de la méthode à tester)
        String result = assessmentService.generateAssessment(patientId);

        // ASSERT (Vérification du résultat)
        assertEquals("None", result);
    }

    /**
     * Teste le cas "Borderline" (Risque limité).
     * Conditions : Patient de plus de 30 ans avec entre 2 et 5 déclencheurs.
     */
    @Test
    void generateAssessment_shouldReturnBorderline_whenOver30And2Triggers() {
        // ARRANGE
        Long patientId = 1L;
        Patient mockPatient = new Patient(patientId, "Test", "Borderline", LocalDate.of(1950, 1, 1), "M");
        // Notes contenant 2 déclencheurs : "fumeur" et "vertiges"
        List<Note> mockNotes = Arrays.asList(
                new Note("id1", "Patient fumeur occasionnel"),
                new Note("id2", "Se plaint de vertiges fréquents")
        );

        when(patientProxy.getPatientById(anyLong())).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(anyLong())).thenReturn(mockNotes);

        // ACT
        String result = assessmentService.generateAssessment(patientId);

        // ASSERT
        assertEquals("Borderline", result);
    }

    /**
     * Teste le cas "In Danger" (Danger).
     * Conditions : Homme de moins de 30 ans avec 3 déclencheurs.
     */
    @Test
    void generateAssessment_shouldReturnInDanger_whenMaleUnder30And3Triggers() {
        // ARRANGE
        Long patientId = 1L;
        // Patient de 20 ans (Date actuelle - 20 ans)
        LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);
        Patient mockPatient = new Patient(patientId, "Test", "Danger", twentyYearsAgo, "M");

        // Notes avec 3 déclencheurs : "rechute", "anormal", "réaction"
        List<Note> mockNotes = Arrays.asList(
                new Note("id1", "Risque de rechute élevé"),
                new Note("id2", "Bilan sanguin anormal"),
                new Note("id3", "Forte réaction aux allergènes")
        );

        when(patientProxy.getPatientById(anyLong())).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(anyLong())).thenReturn(mockNotes);

        // ACT
        String result = assessmentService.generateAssessment(patientId);

        // ASSERT
        assertEquals("In Danger", result);
    }

    /**
     * Teste le cas "Early onset" (Apparition précoce).
     * Conditions : Femme de moins de 30 ans avec 7 déclencheurs ou plus.
     */
    @Test
    void generateAssessment_shouldReturnEarlyOnset_whenFemaleUnder30And7Triggers() {
        // ARRANGE
        Long patientId = 1L;
        LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);
        Patient mockPatient = new Patient(patientId, "Test", "Early", twentyYearsAgo, "F");

        // Création de notes contenant 7 déclencheurs distincts
        List<Note> mockNotes = Arrays.asList(
                new Note("1", "Hémoglobine A1C élevée"),
                new Note("2", "Microalbumine présente"),
                new Note("3", "Taille anormale"),
                new Note("4", "Poids fluctuant"),
                new Note("5", "Fumeur passif"),
                new Note("6", "Cholestérol en hausse"),
                new Note("7", "Anticorps détectés")
        );

        when(patientProxy.getPatientById(anyLong())).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(anyLong())).thenReturn(mockNotes);

        // ACT
        String result = assessmentService.generateAssessment(patientId);

        // ASSERT
        assertEquals("Early onset", result);
    }
}