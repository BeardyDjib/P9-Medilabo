package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.NoteDto;
import com.medilabo.client_ui.dto.PatientDto;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test unitaire pour le ClientController.
 * <p>
 * Ces tests vérifient le bon fonctionnement des endpoints du contrôleur MVC (Thymeleaf),
 * en simulant les appels aux microservices externes via des Mocks.
 * La sécurité est désactivée pour se concentrer sur la logique métier du contrôleur.
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

    // --- TESTS D'AFFICHAGE (GET) ---

    /**
     * Teste l'affichage de la page d'accueil.
     * Vérifie que la liste des patients est bien récupérée via le proxy et transmise à la vue.
     */
    @Test
    public void testAccueil() throws Exception {
        // GIVEN : Initialisation des données simulées
        PatientDto p1 = new PatientDto();
        p1.setId(1L);
        p1.setNom("Doe");
        p1.setPrenom("John");
        List<PatientDto> patients = Arrays.asList(p1);

        when(patientProxy.patients(anyString())).thenReturn(patients);

        // WHEN : Exécution de la requête GET sur la racine
        mockMvc.perform(get("/"))

                // THEN : Vérification du statut, de la vue et du contenu
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(content().string(containsString("Doe")));
    }

    /**
     * Teste l'affichage de la fiche d'un patient.
     * Vérifie que les informations du patient (SQL) et ses notes (Mongo) sont agrégées.
     */
    @Test
    public void testFichePatient() throws Exception {
        // GIVEN : Préparation d'un patient et d'une note
        PatientDto p = new PatientDto();
        p.setId(1L);
        p.setNom("Doe");

        NoteDto n = new NoteDto();
        n.setNote("Patient en forme");

        when(patientProxy.getPatient(anyLong(), anyString())).thenReturn(p);
        when(noteProxy.getNotesByPatientId(anyInt(), anyString())).thenReturn(Collections.singletonList(n));

        // WHEN : Demande d'affichage de la fiche patient n°1
        mockMvc.perform(get("/patient/1"))

                // THEN : Vérification que les deux modèles sont bien présents
                .andExpect(status().isOk())
                .andExpect(view().name("fiche_patient"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(content().string(containsString("Patient en forme")));
    }

    /**
     * Teste l'affichage du formulaire de modification d'un patient.
     * Vérifie que le formulaire est pré-rempli avec les données existantes.
     */
    @Test
    public void testUpdatePatientForm() throws Exception {
        // GIVEN : Un patient existant renvoyé par le proxy
        PatientDto p = new PatientDto();
        p.setId(1L);
        p.setNom("Doe");

        when(patientProxy.getPatient(anyLong(), anyString())).thenReturn(p);

        // WHEN : Accès à la page de modification
        mockMvc.perform(get("/patient/update/1"))

                // THEN : Vérification de la vue retournée
                .andExpect(status().isOk())
                .andExpect(view().name("patient_update"))
                .andExpect(model().attributeExists("patient"));
    }

    /**
     * Teste l'affichage du formulaire d'ajout de note.
     * Vérifie simplement que la page s'affiche avec un objet note vide.
     */
    @Test
    public void testAddNoteForm() throws Exception {
        // GIVEN : (Pas de pré-requis externe nécessaire ici)

        // WHEN : Accès au formulaire d'ajout
        mockMvc.perform(get("/patient/1/note/add"))

                // THEN : Vérification que la page est correcte
                .andExpect(status().isOk())
                .andExpect(view().name("note_add"))
                .andExpect(model().attributeExists("note"));
    }

    // --- TESTS DE SOUMISSION (POST) ---

    /**
     * Teste la soumission du formulaire de modification.
     * Vérifie que le contrôleur appelle bien le proxy pour sauvegarder et redirige l'utilisateur.
     */
    @Test
    public void testUpdatePatient() throws Exception {
        // GIVEN : Données du formulaire soumis
        PatientDto p = new PatientDto();
        p.setId(1L);
        p.setNom("DoeUpdated");

        when(patientProxy.updatePatient(anyLong(), any(PatientDto.class), anyString())).thenReturn(p);

        // WHEN : Envoi du formulaire en POST
        mockMvc.perform(post("/patient/update/1")
                        .flashAttr("patient", p))

                // THEN : Vérification de la redirection et de l'appel au service
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1"));

        verify(patientProxy).updatePatient(anyLong(), any(PatientDto.class), anyString());
    }

    /**
     * Teste la soumission du formulaire d'ajout de note.
     * Vérifie que la note est transmise au microservice Note et que l'utilisateur est redirigé.
     */
    @Test
    public void testSaveNote() throws Exception {
        // GIVEN : Une nouvelle note à sauvegarder
        NoteDto n = new NoteDto();
        n.setPatId(1);
        n.setNote("Nouvelle note");

        when(noteProxy.addNote(any(NoteDto.class), anyString())).thenReturn(n);

        // WHEN : Envoi du formulaire en POST
        mockMvc.perform(post("/patient/note/save")
                        .flashAttr("note", n))

                // THEN : Vérification de la redirection et de l'appel au service Note
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1"));

        verify(noteProxy).addNote(any(NoteDto.class), anyString());
    }
}