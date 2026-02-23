package com.medilabo.assessment_service.service;

import com.medilabo.assessment_service.model.Note;
import com.medilabo.assessment_service.model.Patient;
import com.medilabo.assessment_service.proxies.NoteProxy;
import com.medilabo.assessment_service.proxies.PatientProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Classe de tests unitaires validant la logique de calcul des risques de diabète.
 */
@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @Mock
    private PatientProxy patientProxy;

    @Mock
    private NoteProxy noteProxy;

    private AssessmentService assessmentService;

    /**
     * Initialise le service avec ses dépendances mockées et la liste des déclencheurs avant chaque test.
     */
    @BeforeEach
    void setUp() {
        List<String> mockTriggers = Arrays.asList(
                "hémoglobine a1c", "microalbumine", "taille", "poids", "fumeur",
                "anormal", "cholestérol", "vertiges", "rechute", "réaction", "anticorps"
        );
        assessmentService = new AssessmentService(patientProxy, noteProxy, mockTriggers);
    }

    /**
     * Vérifie que le diagnostic "None" est retourné lorsqu'aucun déclencheur n'est présent.
     */
    @Test
    void generateAssessment_shouldReturnNone_whenNoRisk() {
        Long patientId = 1L;
        Patient mockPatient = new Patient(patientId, "Test", "None", LocalDate.of(1970, 1, 1), "F");
        List<Note> mockNotes = Collections.singletonList(new Note("id1", "Le patient va bien."));

        when(patientProxy.getPatientById(patientId)).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(patientId)).thenReturn(mockNotes);

        String result = assessmentService.generateAssessment(patientId);

        assertEquals("None", result);
    }

    /**
     * Vérifie que le diagnostic "Borderline" est retourné pour un patient de plus de 30 ans avec 2 déclencheurs.
     */
    @Test
    void generateAssessment_shouldReturnBorderline_whenOver30And2Triggers() {
        Long patientId = 1L;
        Patient mockPatient = new Patient(patientId, "Test", "Borderline", LocalDate.of(1950, 1, 1), "M");
        List<Note> mockNotes = Arrays.asList(
                new Note("id1", "Patient fumeur occasionnel"),
                new Note("id2", "Se plaint de vertiges fréquents")
        );

        when(patientProxy.getPatientById(anyLong())).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(anyLong())).thenReturn(mockNotes);

        String result = assessmentService.generateAssessment(patientId);

        assertEquals("Borderline", result);
    }

    /**
     * Vérifie que le diagnostic "In Danger" est retourné pour un homme de moins de 30 ans avec 3 déclencheurs.
     */
    @Test
    void generateAssessment_shouldReturnInDanger_whenMaleUnder30And3Triggers() {
        Long patientId = 1L;
        LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);
        Patient mockPatient = new Patient(patientId, "Test", "Danger", twentyYearsAgo, "M");
        List<Note> mockNotes = Arrays.asList(
                new Note("id1", "Risque de rechute élevé"),
                new Note("id2", "Bilan sanguin anormal"),
                new Note("id3", "Forte réaction aux allergènes")
        );

        when(patientProxy.getPatientById(anyLong())).thenReturn(mockPatient);
        when(noteProxy.getNotesByPatientId(anyLong())).thenReturn(mockNotes);

        String result = assessmentService.generateAssessment(patientId);

        assertEquals("In Danger", result);
    }

    /**
     * Vérifie que le diagnostic "Early onset" est retourné pour une femme de moins de 30 ans avec 7 déclencheurs.
     */
    @Test
    void generateAssessment_shouldReturnEarlyOnset_whenFemaleUnder30And7Triggers() {
        Long patientId = 1L;
        LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);
        Patient mockPatient = new Patient(patientId, "Test", "Early", twentyYearsAgo, "F");
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

        String result = assessmentService.generateAssessment(patientId);

        assertEquals("Early onset", result);
    }
}