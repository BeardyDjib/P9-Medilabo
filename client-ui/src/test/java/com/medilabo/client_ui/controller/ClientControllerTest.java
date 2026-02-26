package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.NoteDto;
import com.medilabo.client_ui.dto.PatientDto;
import com.medilabo.client_ui.proxies.MicroserviceAssessmentProxy;
import com.medilabo.client_ui.proxies.MicroserviceNoteProxy;
import com.medilabo.client_ui.proxies.MicroservicePatientProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de tests unitaires pour le contrôleur ClientController.
 */
@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MicroservicePatientProxy patientProxy;

    @MockitoBean
    private MicroserviceNoteProxy noteProxy;

    @MockitoBean
    private MicroserviceAssessmentProxy assessmentProxy;

    /**
     * Teste l'accès à la page d'accueil et la récupération de la liste des patients.
     *
     * @throws Exception En cas d'erreur lors de l'exécution de la requête simulée.
     */
    @Test
    public void testAccueil() throws Exception {
        PatientDto p1 = new PatientDto();
        p1.setId(1L);
        p1.setNom("Doe");
        List<PatientDto> patients = Arrays.asList(p1);

        when(patientProxy.patients()).thenReturn(patients);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(content().string(containsString("Doe")));
    }

    /**
     * Teste l'affichage de la fiche détaillée d'un patient avec ses notes et son risque.
     *
     * @throws Exception En cas d'erreur lors de l'exécution de la requête simulée.
     */
    @Test
    public void testFichePatient() throws Exception {
        PatientDto p = new PatientDto();
        p.setId(1L);
        p.setNom("Doe");
        NoteDto n = new NoteDto();
        n.setNote("Patient en forme");

        when(patientProxy.getPatient(anyLong())).thenReturn(p);
        when(noteProxy.getNotesByPatientId(anyInt())).thenReturn(Collections.singletonList(n));
        when(assessmentProxy.getAssessment(anyLong())).thenReturn("None");

        mockMvc.perform(get("/patient/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("fiche_patient"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(content().string(containsString("Patient en forme")));
    }

    /**
     * Teste l'affichage du formulaire de mise à jour d'un patient.
     *
     * @throws Exception En cas d'erreur lors de l'exécution de la requête simulée.
     */
    @Test
    public void testUpdatePatientForm() throws Exception {
        PatientDto p = new PatientDto();
        p.setId(1L);

        when(patientProxy.getPatient(anyLong())).thenReturn(p);

        mockMvc.perform(get("/patient/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient_update"))
                .andExpect(model().attributeExists("patient"));
    }

    /**
     * Teste l'enregistrement des modifications d'un patient et la redirection.
     *
     * @throws Exception En cas d'erreur lors de l'exécution de la requête simulée.
     */
    @Test
    public void testUpdatePatient() throws Exception {
        PatientDto p = new PatientDto();
        p.setId(1L);

        when(patientProxy.updatePatient(anyLong(), any(PatientDto.class))).thenReturn(p);

        mockMvc.perform(post("/patient/update/1")
                        .flashAttr("patient", p))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1"));

        verify(patientProxy).updatePatient(anyLong(), any(PatientDto.class));
    }

    /**
     * Teste la sauvegarde d'une nouvelle note et la redirection.
     *
     * @throws Exception En cas d'erreur lors de l'exécution de la requête simulée.
     */
    @Test
    public void testSaveNote() throws Exception {
        NoteDto n = new NoteDto();
        n.setPatId(1);

        when(noteProxy.addNote(any(NoteDto.class))).thenReturn(n);

        mockMvc.perform(post("/patient/note/save")
                        .flashAttr("note", n))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1"));

        verify(noteProxy).addNote(any(NoteDto.class));
    }
}