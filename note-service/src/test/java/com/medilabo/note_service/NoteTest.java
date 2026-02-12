package com.medilabo.note_service.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour valider le comportement de l'entité Note.
 */
public class NoteTest {

    /**
     * Vérifie le bon fonctionnement des accesseurs (getters) et mutateurs (setters).
     */
    @Test
    public void testNoteGettersAndSetters() {
        Note note = new Note();
        LocalDateTime now = LocalDateTime.now();

        note.setId("123");
        note.setPatId(1);
        note.setPatient("John Doe");
        note.setNote("Medical note content");
        note.setDate(now);

        assertEquals("123", note.getId());
        assertEquals(1, note.getPatId());
        assertEquals("John Doe", note.getPatient());
        assertEquals("Medical note content", note.getNote());
        assertEquals(now, note.getDate());
    }

    /**
     * Vérifie que le constructeur complet initialise correctement tous les attributs.
     */
    @Test
    public void testNoteAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        Note note = new Note("123", 1, "John Doe", "Content", now);

        assertEquals("123", note.getId());
        assertEquals(1, note.getPatId());
        assertEquals("John Doe", note.getPatient());
        assertEquals("Content", note.getNote());
        assertEquals(now, note.getDate());
    }

    /**
     * Vérifie l'égalité logique entre deux objets Note et la cohérence du code de hachage.
     */
    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Note note1 = new Note("1", 1, "P1", "N1", now);
        Note note2 = new Note("1", 1, "P1", "N1", now);
        Note note3 = new Note("2", 2, "P2", "N2", now);

        assertEquals(note1, note2);
        assertEquals(note1.hashCode(), note2.hashCode());
        assertNotEquals(note1, note3);
    }

    /**
     * Vérifie que la méthode toString contient les informations essentielles de l'objet.
     */
    @Test
    public void testToString() {
        Note note = new Note("1", 1, "Patient", "Note", null);

        String stringResult = note.toString();

        assertTrue(stringResult.contains("Patient"));
        assertTrue(stringResult.contains("Note"));
    }
}