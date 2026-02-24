package com.medilabo.note_service.repository;

import com.medilabo.note_service.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Repository pour l'accès aux données des notes médicales.
 * <p>
 * Utilise Spring Data MongoDB pour la persistance documentaire (NoSQL).
 * L'identifiant de l'entité Note est de type {@link String} afin de stocker
 * l'ObjectId (chaîne hexadécimale) généré automatiquement par MongoDB.
 * </p>
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Recherche l'historique complet des notes pour un patient spécifique.
     * La requête MongoDB est générée automatiquement par Spring Data via
     * la convention de nommage de la méthode (findBy + PatId).
     *
     * @param patId L'identifiant technique (SQL) du patient associé.
     * @return      Une liste d'objets Note.
     */
    List<Note> findByPatId(Integer patId);
}