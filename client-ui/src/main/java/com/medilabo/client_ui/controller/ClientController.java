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
 * Contrôleur principal de l'application cliente (Front-End).
 * <p>
 * Ce composant agit comme un orchestrateur : il reçoit les requêtes de l'utilisateur,
 * interroge les différents microservices (Patient, Note, Assessment) via des proxies Feign,
 * et prépare les modèles de données pour les vues Thymeleaf.
 * </p>
 */
@Controller
public class ClientController {

    private final MicroservicePatientProxy patientProxy;
    private final MicroserviceNoteProxy noteProxy;
    private final MicroserviceAssessmentProxy assessmentProxy;

    /**
     * Constructeur avec injection des dépendances requises.
     *
     * @param patientProxy Proxy pour communiquer avec le service de gestion des patients.
     * @param noteProxy Proxy pour communiquer avec le service de gestion des notes.
     * @param assessmentProxy Proxy pour communiquer avec le service d'évaluation du risque.
     */
    public ClientController(MicroservicePatientProxy patientProxy,
                            MicroserviceNoteProxy noteProxy,
                            MicroserviceAssessmentProxy assessmentProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
        this.assessmentProxy = assessmentProxy;
    }

    // --- ACCUEIL ---

    /**
     * Affiche la page d'accueil listant tous les patients.
     *
     * @param model Le modèle Spring pour transmettre les données à la vue.
     * @return Le nom de la vue "accueil".
     */
    @RequestMapping("/")
    public String accueil(Model model){
        // En-tête d'authentification Basic (user:password) encodé en Base64
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";

        List<PatientDto> patients = patientProxy.patients(authHeader);
        model.addAttribute("patients", patients);
        return "accueil";
    }

    // --- FICHE PATIENT ---

    /**
     * Affiche la fiche détaillée d'un patient, incluant ses notes et son évaluation de risque.
     * <p>
     * Cette méthode agrège les données provenant de trois sources distinctes :
     * 1. Les informations administratives (Patient-Service).
     * 2. L'historique médical (Note-Service).
     * 3. L'analyse du risque de diabète (Assessment-Service).
     * </p>
     *
     * @param id L'identifiant unique du patient.
     * @param model Le modèle Spring pour transmettre les données à la vue.
     * @return Le nom de la vue "fiche_patient".
     */
    @GetMapping("/patient/{id}")
    public String fichePatient(@PathVariable("id") long id, Model model) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";

        // 1. Récupération des infos patient
        PatientDto patient = patientProxy.getPatient(id, authHeader);

        // 2. Récupération des notes
        List<NoteDto> notes = noteProxy.getNotesByPatientId((int) id, authHeader);

        // 3. Calcul du risque de diabète (NOUVEAU)
        String risque;
        try {
            // Appel au microservice d'évaluation
            risque = assessmentProxy.getAssessment(id, authHeader);
        } catch (Exception e) {
            // Gestion de repli en cas d'indisponibilité du service
            risque = "Indisponible";
        }

        // Ajout des attributs au modèle
        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("risque", risque); // Transmis à la vue HTML

        return "fiche_patient";
    }

    // --- AJOUTER UNE NOTE ---

    /**
     * Affiche le formulaire d'ajout d'une nouvelle note pour un patient.
     *
     * @param id L'identifiant du patient concerné.
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
     * Traite la soumission du formulaire d'ajout de note.
     *
     * @param note L'objet NoteDto contenant les données saisies.
     * @return Une redirection vers la fiche du patient mis à jour.
     */
    @PostMapping("/patient/note/save")
    public String saveNote(@ModelAttribute("note") NoteDto note) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        note.setDate(LocalDateTime.now());
        noteProxy.addNote(note, authHeader);
        return "redirect:/patient/" + note.getPatId();
    }

    // --- MODIFIER LE PATIENT ---

    /**
     * Affiche le formulaire de modification des informations d'un patient.
     *
     * @param id L'identifiant du patient à modifier.
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
     * Enregistre les modifications apportées aux informations d'un patient.
     *
     * @param id L'identifiant du patient.
     * @param patient L'objet PatientDto contenant les nouvelles données.
     * @return Une redirection vers la fiche du patient mis à jour.
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Long id, @ModelAttribute("patient") PatientDto patient) {
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";
        patientProxy.updatePatient(id, patient, authHeader);
        return "redirect:/patient/" + id;
    }
}