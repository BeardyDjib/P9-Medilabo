package com.medilabo.client_ui.dto;

import java.time.LocalDateTime;


public class NoteDto {

    private String id;
    private Integer patId;
    private String patient;
    private String note;
    private LocalDateTime date;

    public NoteDto() {
    }

    /**
     * Constructeur complet permettant d'initialiser tous les attributs de la note.
     *
     * @param id      L'identifiant technique de la note.
     * @param patId   L'identifiant du patient.
     * @param patient Le nom du patient.
     * @param note    Le contenu de la note.
     * @param date    La date d'enregistrement.
     */
    public NoteDto(String id, Integer patId, String patient, String note, LocalDateTime date) {
        this.id = id;
        this.patId = patId;
        this.patient = patient;
        this.note = note;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getPatId() { return patId; }
    public void setPatId(Integer patId) { this.patId = patId; }

    public String getPatient() { return patient; }
    public void setPatient(String patient) { this.patient = patient; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}