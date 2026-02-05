package com.medilabo.patient_service.controller;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Remplacement officiel de @MockBean
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test pour PatientController.
 * Utilise @WebMvcTest pour ne charger que la couche Web (isolation).
 * Remplace @MockBean par @MockitoBean pour Spring Boot 3.4+.
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simule les requêtes HTTP sans lancer de serveur Tomcat

    @MockitoBean
    private PatientRepository patientRepository; // Mock de la couche données (Simulation)

    /**
     * Teste la récupération de tous les patients.
     * URL : GET /api/patients
     * Résultat attendu : Liste JSON et statut 200 OK.
     */
    @Test
    @WithMockUser(username = "user") // Simule l'authentification Spring Security
    public void testGetAllPatients() throws Exception {
        // GIVEN : On prépare le comportement du Mock (la fausse BDD)
        Patient p1 = new Patient();
        p1.setId(1L);
        p1.setNom("TestNom");

        Patient p2 = new Patient();
        p2.setId(2L);
        p2.setNom("TestNom2");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // WHEN : On exécute la requête HTTP simulée
        mockMvc.perform(get("/api/patients"))
                // THEN : On vérifie les assertions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nom").value("TestNom"));
    }

    /**
     * Teste la récupération d'un patient par ID.
     * URL : GET /api/patients/{id}
     * Résultat attendu : Objet JSON du patient et 200 OK.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetPatientById() throws Exception {
        // GIVEN
        Patient p = new Patient();
        p.setId(1L);
        p.setPrenom("John");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(p));

        // WHEN
        mockMvc.perform(get("/api/patients/1"))
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom").value("John"));
    }

    /**
     * Teste la récupération d'un patient inexistant.
     * URL : GET /api/patients/99
     * Résultat attendu : 404 Not Found.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetPatientByIdNotFound() throws Exception {
        // GIVEN
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        mockMvc.perform(get("/api/patients/99"))
                // THEN
                .andExpect(status().isNotFound());
    }
}