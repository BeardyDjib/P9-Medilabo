package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.PatientDto;
import com.medilabo.client_ui.proxies.MicroservicePatientProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Le nouvel import
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitaire du ClientController.
 * Vérifie l'affichage Thymeleaf en bouchonnant (Mock) l'appel au service externe.
 */
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Remplace @MockBean
    private MicroservicePatientProxy patientProxy;

    /**
     * Teste l'affichage de la page d'accueil avec des patients.
     * Vérifie que le contrôleur passe bien les données à la vue.
     */
    @Test
    public void testAccueil() throws Exception {
        // GIVEN : On prépare un DTO simulé venant du Proxy
        PatientDto p1 = new PatientDto();
        p1.setNom("Doe");
        p1.setPrenom("John");

        List<PatientDto> patients = Arrays.asList(p1);

        // On dit au mock : "Quand on te demande des patients, renvoie cette liste"
        when(patientProxy.patients(anyString())).thenReturn(patients);

        // WHEN : L'utilisateur demande la page racine
        mockMvc.perform(get("/"))
                // THEN : Vérifications
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(content().string(containsString("Doe")));
    }
}