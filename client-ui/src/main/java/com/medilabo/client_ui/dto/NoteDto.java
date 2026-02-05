package com.medilabo.client_ui.dto;

import java.time.LocalDateTime;

public class NoteDto {

    private String id;
    private Integer patId;
    private String patient;
    private String note;
    private LocalDateTime date;

    // --- 1. Constructeur Vide (Obligatoire) ---
    public NoteDto() {
    }

    // --- 2. Constructeur Complet (Pratique) ---
    public NoteDto(String id, Integer patId, String patient, String note, LocalDateTime date) {
        this.id = id;
        this.patId = patId;
        this.patient = patient;
        this.note = note;
        this.date = date;
    }

    // --- 3. GETTERS ET SETTERS ---

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