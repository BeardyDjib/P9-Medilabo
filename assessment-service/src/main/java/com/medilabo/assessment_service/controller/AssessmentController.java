package com.medilabo.assessment_service.controller;

import com.medilabo.assessment_service.service.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST exposant l'API d'évaluation du risque de diabète.
 * Ce point d'entrée permet aux applications clientes (ex: client-ui) de solliciter
 * une analyse de risque pour un patient spécifique.
 */
@RestController
@RequestMapping("/assess")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    /**
     * Récupère le rapport de risque de diabète pour un patient donné via son identifiant.
     * @param id Identifiant unique du patient (patId)
     * @return Le niveau de risque sous forme de texte (None, Borderline, In Danger, Early onset)
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getAssessmentByPatientId(@PathVariable("id") Long id) {
        String result = assessmentService.generateAssessment(id);
        return ResponseEntity.ok(result);
    }
}