package com.medilabo.note_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.note_service.controller.NoteController;
import com.medilabo.note_service.model.Note;
import com.medilabo.note_service.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

// Importations statiques pour rendre le code de test plus lisible (ex: status().isOk() au lieu de MockMvcResultMatchers.status().isOk())
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test d'intégration pour le contrôleur NoteController.
 * <p>
 * Utilise @WebMvcTest pour ne charger que la couche Web de Spring Boot,
 * ce qui est plus rapide qu'un chargement complet de l'application.
 * Les dépendances externes (comme la base de données via NoteRepository) sont "mockées" (simulées).
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    /**
     * MockMvc est le point d'entrée principal pour tester les contrôleurs Spring MVC.
     * Il permet d'envoyer des requêtes HTTP simulées et d'asserter les réponses.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * @MockBean crée une simulation (mock) du NoteRepository.
     * Cela nous permet de définir le comportement de la base de données sans avoir besoin d'une vraie connexion MongoDB.
     */
    @MockBean
    private NoteRepository noteRepository;

    /**
     * ObjectMapper est utilisé pour convertir des objets Java en JSON (sérialisation)
     * et vice-versa. Utile pour envoyer des données dans le corps des requêtes POST/PUT.
     */
    @Autowired
    private ObjectMapper objectMapper;

    // Objets de test réutilisables
    private Note note1;
    private Note note2;

    /**
     * S'exécute avant chaque méthode de test (@Test).
     * Permet de réinitialiser les données pour garantir que les tests sont indépendants les uns des autres.
     */
    @BeforeEach
    public void setup() {
        note1 = new Note();
        note1.setId("1");
        note1.setPatId(1);
        note1.setPatient("Test Patient");
        note1.setNote("Patient states that they are feeling a lot better.");
        note1.setDate(LocalDateTime.now());

        note2 = new Note();
        note2.setId("2");
        note2.setPatId(2);
        note2.setPatient("Another Patient");
        note2.setNote("Patient reports dizziness.");
        note2.setDate(LocalDateTime.now());
    }

    /**
     * Teste la récupération de toutes les notes (GET /notes).
     * <p>
     * Scénario :
     * 1. On dit au MockRepository de renvoyer une liste de 2 notes.
     * 2. On effectue une requête GET sur /notes.
     * 3. On vérifie que le statut est 200 OK.
     * 4. On vérifie que le JSON retourné contient bien 2 éléments.
     */
    @Test
    public void getAllNotes_shouldReturnList() throws Exception {
        // GIVEN
        List<Note> allNotes = List.of(note1, note2);
        given(noteRepository.findAll()).willReturn(allNotes);

        // WHEN & THEN
        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // "$" représente la racine du JSON (la liste)
                .andExpect(jsonPath("$[0].patient", is(note1.getPatient()))); // Vérifie le nom du premier patient
    }

    /**
     * Teste la récupération des notes d'un patient spécifique (GET /notes/patient/{id}).
     * <p>
     * Scénario :
     * 1. On dit au MockRepository : "Si on te demande les notes du patId 1, renvoie cette liste".
     * 2. On appelle l'URL avec l'ID 1.
     * 3. On vérifie qu'on reçoit bien la note correspondante.
     */
    @Test
    public void getNotesByPatId_shouldReturnNotes() throws Exception {
        // GIVEN
        given(noteRepository.findByPatId(1)).willReturn(List.of(note1));

        // WHEN & THEN
        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].note", is(note1.getNote())));
    }

    /**
     * Teste l'ajout d'une nouvelle note (POST /notes).
     * <p>
     * Scénario :
     * 1. On simule la sauvegarde : quand on appelle save(), on renvoie l'objet sauvegardé.
     * 2. On envoie une requête POST avec l'objet note1 converti en JSON dans le corps (body).
     * 3. On vérifie que le contrôleur renvoie 200 OK et l'objet créé.
     */
    @Test
    public void addNote_shouldReturnSavedNote() throws Exception {
        // GIVEN
        // any(Note.class) signifie : "peu importe l'objet Note passé en paramètre, accepte-le"
        given(noteRepository.save(any(Note.class))).willReturn(note1);

        // WHEN & THEN
        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON) // On précise qu'on envoie du JSON
                        .content(objectMapper.writeValueAsString(note1))) // On transforme l'objet Java en String JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient", is(note1.getPatient())));
    }

    /**
     * Teste la mise à jour d'une note existante (PUT /notes).
     * <p>
     * Le fonctionnement est très similaire au POST, mais utilise la méthode HTTP PUT.
     */
    @Test
    public void updateNote_shouldReturnUpdatedNote() throws Exception {
        // GIVEN
        Note updatedNote = new Note();
        updatedNote.setId("1");
        updatedNote.setPatId(1);
        updatedNote.setPatient("Test Patient");
        updatedNote.setNote("Updated note content.");
        updatedNote.setDate(LocalDateTime.now());

        given(noteRepository.save(any(Note.class))).willReturn(updatedNote);

        // WHEN & THEN
        mockMvc.perform(put("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note", is("Updated note content.")));
    }

    /**
     * Teste la suppression d'une note (DELETE /notes/{id}).
     * <p>
     * Scénario :
     * 1. On appelle l'URL de suppression.
     * 2. On vérifie le statut 200 OK.
     * 3. Important : On utilise verify() pour s'assurer que la méthode deleteById du repository
     * a bien été appelée exactement une fois avec le bon ID.
     */
    @Test
    public void deleteNote_shouldReturnOk() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/notes/1"))
                .andExpect(status().isOk());

        // VERIFY (Vérification collatérale)
        // On vérifie que le contrôleur a bien donné l'ordre au repository de supprimer l'ID "1"
        verify(noteRepository).deleteById("1");
    }
}