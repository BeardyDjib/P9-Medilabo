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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de tests unitaires pour le contrôleur NoteController.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Note note1;
    private Note note2;

    /**
     * Initialise les données de test avant chaque méthode.
     */
    @BeforeEach
    public void setup() {
        note1 = new Note("1", 1, "Test Patient", "Note content 1", LocalDateTime.now());
        note2 = new Note("2", 2, "Another Patient", "Note content 2", LocalDateTime.now());
    }

    /**
     * Vérifie la récupération de la liste complète des notes.
     *
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    public void getAllNotes_shouldReturnList() throws Exception {
        given(noteRepository.findAll()).willReturn(List.of(note1, note2));

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].patient", is(note1.getPatient())));
    }

    /**
     * Vérifie la récupération des notes pour un patient spécifique via son identifiant.
     *
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    public void getNotesByPatId_shouldReturnNotes() throws Exception {
        given(noteRepository.findByPatId(1)).willReturn(List.of(note1));

        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].note", is(note1.getNote())));
    }

    /**
     * Vérifie l'ajout d'une nouvelle note.
     *
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    public void addNote_shouldReturnSavedNote() throws Exception {
        given(noteRepository.save(any(Note.class))).willReturn(note1);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient", is(note1.getPatient())));
    }

    /**
     * Vérifie la mise à jour d'une note existante.
     *
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    public void updateNote_shouldReturnUpdatedNote() throws Exception {
        Note updatedNote = new Note("1", 1, "Test Patient", "Updated content", LocalDateTime.now());
        given(noteRepository.save(any(Note.class))).willReturn(updatedNote);

        mockMvc.perform(put("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note", is("Updated content")));
    }

    /**
     * Vérifie la suppression d'une note.
     *
     * @throws Exception En cas d'erreur lors de la requête simulée.
     */
    @Test
    public void deleteNote_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/notes/1"))
                .andExpect(status().isOk());

        verify(noteRepository).deleteById("1");
    }
}