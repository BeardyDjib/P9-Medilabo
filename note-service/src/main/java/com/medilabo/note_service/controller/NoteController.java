package com.medilabo.note_service.controller;

import com.medilabo.note_service.model.Note;
import com.medilabo.note_service.repository.NoteRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contrôleur REST gérant les opérations CRUD sur les notes médicales.
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Enregistre une nouvelle note médicale.
     *
     * @param note L'objet note à sauvegarder.
     * @return     La note enregistrée.
     */
    @PostMapping
    public Note addNote(@RequestBody Note note) {
        return noteRepository.save(note);
    }

    /**
     * Récupère l'intégralité des notes présentes en base.
     * @return Une liste de toutes les notes.
     */
    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    /**
     * Récupère l'historique des notes pour un patient spécifique.
     *
     * @param patId L'identifiant unique du patient.
     * @return      La liste des notes associées.
     */
    @GetMapping("/patient/{patId}")
    public List<Note> getNotesByPatientId(@PathVariable Integer patId) {
        return noteRepository.findByPatId(patId);
    }

    /**
     * Met à jour une note existante.
     *
     * @param note La note contenant les modifications.
     * @return     La note mise à jour.
     */
    @PutMapping
    public Note updateNote(@RequestBody Note note) {
        return noteRepository.save(note);
    }

    /**
     * Supprime une note par son identifiant unique.
     * @param id L'identifiant technique de la note.
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteRepository.deleteById(id);
    }
}