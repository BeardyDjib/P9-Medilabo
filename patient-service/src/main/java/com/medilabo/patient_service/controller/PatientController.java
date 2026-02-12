package com.medilabo.patient_service.controller;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des patients.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Liste tous les patients présents en base de données.
     * @return Une liste d'objets Patient.
     */
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Récupère les informations d'un patient par son ID.
     *
     * @param id L'identifiant du patient.
     * @return Le patient trouvé ou une réponse 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Met à jour les données d'un patient existant.
     *
     * @param id             L'identifiant du patient à modifier.
     * @param patientDetails Les nouvelles informations.
     * @return Le patient mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient introuvable pour l'id :: " + id));

        patient.setPrenom(patientDetails.getPrenom());
        patient.setNom(patientDetails.getNom());
        patient.setDateDeNaissance(patientDetails.getDateDeNaissance());
        patient.setGenre(patientDetails.getGenre());
        patient.setAdresse(patientDetails.getAdresse());
        patient.setTelephone(patientDetails.getTelephone());

        final Patient updatedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(updatedPatient);
    }
}