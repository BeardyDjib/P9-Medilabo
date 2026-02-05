package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.NoteDto;
import com.medilabo.client_ui.dto.PatientDto;
import com.medilabo.client_ui.proxies.MicroserviceNoteProxy;
import com.medilabo.client_ui.proxies.MicroservicePatientProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ClientController {

    private final MicroservicePatientProxy patientProxy;
    private final MicroserviceNoteProxy noteProxy;

    public ClientController(MicroservicePatientProxy patientProxy, MicroserviceNoteProxy noteProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
    }

    // --- ACCUEIL ---
    @RequestMapping("/")
    public String accueil(Model model){
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        List<PatientDto> patients = patientProxy.patients(authHeader);
        model.addAttribute("patients", patients);
        return "accueil";
    }

    // --- FICHE PATIENT ---
    @GetMapping("/patient/{id}")
    public String fichePatient(@PathVariable("id") long id, Model model) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        PatientDto patient = patientProxy.getPatient(id, authHeader);
        List<NoteDto> notes = noteProxy.getNotesByPatientId((int) id, authHeader);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        return "fiche_patient";
    }

    // --- AJOUTER UNE NOTE ---
    @GetMapping("/patient/{id}/note/add")
    public String addNoteForm(@PathVariable("id") Integer id, Model model) {
        NoteDto note = new NoteDto();
        note.setPatId(id);
        model.addAttribute("note", note);
        return "note_add";
    }

    @PostMapping("/patient/note/save")
    public String saveNote(@ModelAttribute("note") NoteDto note) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        note.setDate(LocalDateTime.now());
        noteProxy.addNote(note, authHeader);
        return "redirect:/patient/" + note.getPatId();
    }

    // --- MODIFIER LE PATIENT ---

    // 1. Afficher le formulaire
    @GetMapping("/patient/update/{id}")
    public String updatePatientForm(@PathVariable("id") Long id, Model model) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        // On récupère le patient pour pré-remplir les champs
        PatientDto patient = patientProxy.getPatient(id, authHeader);
        model.addAttribute("patient", patient);
        return "patient_update";
    }

    // 2. Enregistrer les modifs
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Long id, @ModelAttribute("patient") PatientDto patient) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        patientProxy.updatePatient(id, patient, authHeader);
        return "redirect:/patient/" + id;
    }
}