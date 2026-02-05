package com.medilabo.patient_service.controller;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Endpoint pour récupérer la liste de tous les patients
     * URL : GET http://localhost:9001/api/patients
     */
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Endpoint pour récupérer un patient par son ID
     * URL : GET http://localhost:9001/api/patients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        // On cherche le patient en base
        Optional<Patient> patient = patientRepository.findById(id);

        // S'il existe, on renvoie 200 OK avec le patient
        // Sinon, on renvoie 404 Not Found
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}