package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.NoteDto;
import com.medilabo.client_ui.dto.PatientDto;
import com.medilabo.client_ui.proxies.MicroserviceAssessmentProxy;
import com.medilabo.client_ui.proxies.MicroserviceNoteProxy;
import com.medilabo.client_ui.proxies.MicroservicePatientProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur gérant les interactions entre l'interface utilisateur et les microservices.
 */
@Controller
public class ClientController {

    private final MicroservicePatientProxy patientProxy;
    private final MicroserviceNoteProxy noteProxy;
    private final MicroserviceAssessmentProxy assessmentProxy;

    /**
     * Constructeur injectant les proxys de communication.
     *
     * @param patientProxy    Proxy pour le service Patient.
     * @param noteProxy       Proxy pour le service Note.
     * @param assessmentProxy Proxy pour le service Assessment.
     */
    public ClientController(MicroservicePatientProxy patientProxy,
                            MicroserviceNoteProxy noteProxy,
                            MicroserviceAssessmentProxy assessmentProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
        this.assessmentProxy = assessmentProxy;
    }

    /**
     * Affiche la liste globale des patients.
     *
     * @param model Le modèle Spring.
     * @return Le nom de la vue "accueil".
     */
    @RequestMapping("/")
    public String accueil(Model model){
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        List<PatientDto> patients = patientProxy.patients(authHeader);
        model.addAttribute("patients", patients);
        return "accueil";
    }

    /**
     * Affiche les détails d'un patient, ses notes et son niveau de risque.
     *
     * @param id    L'identifiant du patient.
     * @param model Le modèle Spring.
     * @return Le nom de la vue "fiche_patient".
     */
    @GetMapping("/patient/{id}")
    public String fichePatient(@PathVariable("id") long id, Model model) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";

        PatientDto patient = patientProxy.getPatient(id, authHeader);
        List<NoteDto> notes = noteProxy.getNotesByPatientId((int) id, authHeader);

        String risque;
        try {
            risque = assessmentProxy.getAssessment(id, authHeader);
        } catch (Exception e) {
            risque = "Indisponible";
        }

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("risque", risque);

        return "fiche_patient";
    }

    /**
     * Affiche le formulaire pour ajouter une note.
     *
     * @param id    L'identifiant du patient.
     * @param model Le modèle Spring.
     * @return Le nom de la vue "note_add".
     */
    @GetMapping("/patient/{id}/note/add")
    public String addNoteForm(@PathVariable("id") Integer id, Model model) {
        NoteDto note = new NoteDto();
        note.setPatId(id);
        model.addAttribute("note", note);
        return "note_add";
    }

    /**
     * Enregistre une nouvelle note.
     *
     * @param note Les données de la note.
     * @return Une redirection vers la fiche patient.
     */
    @PostMapping("/patient/note/save")
    public String saveNote(@ModelAttribute("note") NoteDto note) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        note.setDate(LocalDateTime.now());
        noteProxy.addNote(note, authHeader);
        return "redirect:/patient/" + note.getPatId();
    }

    /**
     * Affiche le formulaire de modification d'un patient.
     *
     * @param id    L'identifiant du patient.
     * @param model Le modèle Spring.
     * @return Le nom de la vue "patient_update".
     */
    @GetMapping("/patient/update/{id}")
    public String updatePatientForm(@PathVariable("id") Long id, Model model) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        PatientDto patient = patientProxy.getPatient(id, authHeader);
        model.addAttribute("patient", patient);
        return "patient_update";
    }

    /**
     * Enregistre les modifications d'un patient.
     *
     * @param id      L'identifiant du patient.
     * @param patient Les nouvelles données.
     * @return Une redirection vers la fiche patient.
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Long id, @ModelAttribute("patient") PatientDto patient) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        patientProxy.updatePatient(id, patient, authHeader);
        return "redirect:/patient/" + id;
    }
}