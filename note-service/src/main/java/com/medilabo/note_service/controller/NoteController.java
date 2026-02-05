package com.medilabo.note_service.controller;

import com.medilabo.note_service.model.Note;
import com.medilabo.note_service.repository.NoteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Ajouter une note
    @PostMapping
    public Note addNote(@RequestBody Note note) {
        return noteRepository.save(note);
    }

    // Récupérer toutes les notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Récupérer les notes d'un patient par son ID (CRITIQUE pour le projet)
    @GetMapping("/patient/{patId}")
    public List<Note> getNotesByPatientId(@PathVariable Integer patId) {
        return noteRepository.findByPatId(patId);
    }

    // Modifier une note
    @PutMapping
    public Note updateNote(@RequestBody Note note) {
        return noteRepository.save(note);
    }

    // Supprimer une note
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteRepository.deleteById(id);
    }
}