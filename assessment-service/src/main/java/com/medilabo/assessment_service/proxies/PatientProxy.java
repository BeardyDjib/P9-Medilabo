package com.medilabo.assessment_service.proxies;

import com.medilabo.assessment_service.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy Feign pour la communication avec le microservice de gestion des patients.
 */
@FeignClient(name = "patient-service", url = "${patient-service.url:http://localhost:9001}")
public interface PatientProxy {

    /**
     * Récupère les informations d'un patient par son identifiant unique.
     *
     * @param id L'identifiant du patient.
     * @return   Un objet Patient contenant les données administratives.
     */
    @GetMapping("/api/patients/{id}")
    Patient getPatientById(@PathVariable("id") Long id);
}