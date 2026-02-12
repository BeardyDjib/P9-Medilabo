package com.medilabo.note_service.repository;

import com.medilabo.note_service.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface Repository pour l'accès aux données des notes dans MongoDB.
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Recherche les notes par identifiant patient.
     *
     * @param patId L'identifiant du patient.
     * @return      Une liste de notes.
     */
    List<Note> findByPatId(Integer patId);
}