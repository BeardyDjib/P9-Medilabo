package com.medilabo.assessment_service.controller;

import com.medilabo.assessment_service.service.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST fournissant les analyses de risque de diabète.
 */
@RestController
@RequestMapping("/assess")
public class AssessmentController {

    private final AssessmentService assessmentService;

    /**
     * Constructeur injectant le service d'évaluation.
     *
     * @param assessmentService Le service de calcul des risques.
     */
    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    /**
     * Récupère le niveau de risque calculé pour un patient.
     *
     * @param id L'identifiant unique du patient.
     * @return   Une réponse contenant le libellé du risque.
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getAssessmentByPatientId(@PathVariable("id") Long id) {
        String result = assessmentService.generateAssessment(id);
        return ResponseEntity.ok(result);
    }
}