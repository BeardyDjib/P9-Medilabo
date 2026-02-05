package com.medilabo.patient_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test pour PatientController.
 * Utilise @WebMvcTest pour charger uniquement la couche Web et simuler les échanges HTTP.
 * Les dépendances externes (Repository) sont mockées.
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Nécessaire pour convertir les objets Java en JSON

    @MockitoBean
    private PatientRepository patientRepository;

    /**
     * Teste la récupération de tous les patients.
     * URL : GET /api/patients
     * Résultat attendu : Liste JSON contenant les patients et statut 200 OK.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetAllPatients() throws Exception {
        // GIVEN : Configuration du comportement du repository mocké
        Patient p1 = new Patient();
        p1.setId(1L);
        p1.setNom("TestNom");

        Patient p2 = new Patient();
        p2.setId(2L);
        p2.setNom("TestNom2");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // WHEN : Exécution de la requête GET
        mockMvc.perform(get("/api/patients"))

                // THEN : Vérification du statut et du contenu de la réponse
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nom").value("TestNom"));
    }

    /**
     * Teste la récupération d'un patient existant par son ID.
     * URL : GET /api/patients/{id}
     * Résultat attendu : Objet JSON du patient et statut 200 OK.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetPatientById() throws Exception {
        // GIVEN : Un patient existant en base
        Patient p = new Patient();
        p.setId(1L);
        p.setPrenom("John");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(p));

        // WHEN : Exécution de la requête GET avec l'ID 1
        mockMvc.perform(get("/api/patients/1"))

                // THEN : Vérification que le prénom correspond
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom").value("John"));
    }

    /**
     * Teste la récupération d'un patient inexistant.
     * URL : GET /api/patients/{id}
     * Résultat attendu : Statut 404 Not Found.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetPatientByIdNotFound() throws Exception {
        // GIVEN : Le repository ne trouve rien pour l'ID 99
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN : Exécution de la requête GET avec l'ID 99
        mockMvc.perform(get("/api/patients/99"))

                // THEN : Vérification du statut d'erreur
                .andExpect(status().isNotFound());
    }

    /**
     * Teste la mise à jour d'un patient existant.
     * URL : PUT /api/patients/{id}
     * Résultat attendu : Objet JSON mis à jour et statut 200 OK.
     */
    @Test
    @WithMockUser(username = "user")
    public void testUpdatePatient() throws Exception {
        // GIVEN : Un patient existant et les nouvelles données
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setNom("AncienNom");

        Patient updateInfo = new Patient();
        updateInfo.setNom("NouveauNom");
        updateInfo.setPrenom("Jean");

        // Simulation : on trouve le patient, puis on sauvegarde la version modifiée
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN : Exécution de la requête PUT avec le corps JSON
        mockMvc.perform(put("/api/patients/1")
                        .with(csrf()) // Indispensable pour les tests de modification avec Spring Security
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))

                // THEN : Vérification que le nom a bien été mis à jour dans la réponse
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("NouveauNom"));
    }
}