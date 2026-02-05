package com.medilabo.patient_service.controller;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Récupère la liste complète des patients enregistrés.
     * @return Liste des patients
     */
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Récupère un patient spécifique via son identifiant unique.
     * @param id Identifiant du patient
     * @return Le patient trouvé ou une erreur 404 si inexistant
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientRepository.findById(id);

        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Met à jour les informations d'un patient existant.
     * @param id Identifiant du patient à modifier
     * @param patientDetails Les nouvelles données du patient reçues au format JSON
     * @return Le patient mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        // Recherche du patient existant ou levée d'une exception si non trouvé
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient introuvable pour l'id :: " + id));

        // Mise à jour des champs
        patient.setPrenom(patientDetails.getPrenom());
        patient.setNom(patientDetails.getNom());
        patient.setDateDeNaissance(patientDetails.getDateDeNaissance());
        patient.setGenre(patientDetails.getGenre());
        patient.setAdresse(patientDetails.getAdresse());
        patient.setTelephone(patientDetails.getTelephone());

        // Sauvegarde des modifications en base de données
        final Patient updatedPatient = patientRepository.save(patient);

        return ResponseEntity.ok(updatedPatient);
    }
}