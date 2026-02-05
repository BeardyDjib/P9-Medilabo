package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.PatientDto;
import com.medilabo.client_ui.proxies.MicroserviceNoteProxy;
import com.medilabo.client_ui.proxies.MicroservicePatientProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MicroservicePatientProxy patientProxy;

    @MockitoBean
    private MicroserviceNoteProxy noteProxy;

    @Test
    public void testAccueil() throws Exception {
        // Préparation des données (Given)
        PatientDto p1 = new PatientDto();
        p1.setId(1L);
        p1.setNom("Doe");
        p1.setPrenom("John");

        List<PatientDto> patients = Arrays.asList(p1);

        // Simulation du comportement du proxy
        when(patientProxy.patients(anyString())).thenReturn(patients);

        // Exécution et Vérification (When & Then)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(content().string(containsString("Doe")));
    }
}