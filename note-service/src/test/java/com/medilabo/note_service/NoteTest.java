package com.medilabo.note_service.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    @Test
    public void testNoteGettersAndSetters() {
        // GIVEN
        Note note = new Note();
        LocalDateTime now = LocalDateTime.now();

        // WHEN
        note.setId("123");
        note.setPatId(1);
        note.setPatient("John Doe");
        note.setNote("Medical note content");
        note.setDate(now);

        // THEN
        assertEquals("123", note.getId());
        assertEquals(1, note.getPatId());
        assertEquals("John Doe", note.getPatient());
        assertEquals("Medical note content", note.getNote());
        assertEquals(now, note.getDate());
    }

    @Test
    public void testNoteAllArgsConstructor() {
        // GIVEN
        LocalDateTime now = LocalDateTime.now();

        // WHEN
        Note note = new Note("123", 1, "John Doe", "Content", now);

        // THEN
        assertEquals("123", note.getId());
        assertEquals(1, note.getPatId());
        assertEquals("John Doe", note.getPatient());
        assertEquals("Content", note.getNote());
        assertEquals(now, note.getDate());
    }

    @Test
    public void testEqualsAndHashCode() {
        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        Note note1 = new Note("1", 1, "P1", "N1", now);
        Note note2 = new Note("1", 1, "P1", "N1", now);
        Note note3 = new Note("2", 2, "P2", "N2", now);

        // THEN
        assertEquals(note1, note2); // Test equals
        assertEquals(note1.hashCode(), note2.hashCode()); // Test hashCode
        assertNotEquals(note1, note3);
    }

    @Test
    public void testToString() {
        // GIVEN
        Note note = new Note("1", 1, "Patient", "Note", null);

        // WHEN
        String stringResult = note.toString();

        // THEN
        assertTrue(stringResult.contains("Patient"));
        assertTrue(stringResult.contains("Note"));
    }
}