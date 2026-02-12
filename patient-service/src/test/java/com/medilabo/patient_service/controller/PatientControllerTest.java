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
 * Tests unitaires pour le contrôleur PatientController.
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientRepository patientRepository;

    /**
     * Vérifie la récupération de la liste complète des patients.
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetAllPatients() throws Exception {
        Patient p1 = new Patient();
        p1.setId(1L);
        p1.setNom("TestNom");
        when(patientRepository.findAll()).thenReturn(Arrays.asList(p1));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("TestNom"));
    }

    /**
     * Vérifie la récupération d'un patient par son identifiant.
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetPatientById() throws Exception {
        Patient p = new Patient();
        p.setId(1L);
        p.setPrenom("John");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom").value("John"));
    }

    /**
     * Vérifie le comportement en cas de patient inexistant.
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    @WithMockUser(username = "user")
    public void testGetPatientByIdNotFound() throws Exception {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/99"))
                .andExpect(status().isNotFound());
    }

    /**
     * Vérifie la mise à jour des informations d'un patient.
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    @WithMockUser(username = "user")
    public void testUpdatePatient() throws Exception {
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setNom("AncienNom");

        Patient updateInfo = new Patient();
        updateInfo.setNom("NouveauNom");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/patients/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("NouveauNom"));
    }
}