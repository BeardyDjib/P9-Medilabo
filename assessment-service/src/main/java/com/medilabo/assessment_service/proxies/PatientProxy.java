package com.medilabo.assessment_service.proxies;

import com.medilabo.assessment_service.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy Feign pour la communication avec le microservice Patient-Service.
 */
@FeignClient(name = "patient-service", url = "localhost:9001")
public interface PatientProxy {

    /**
     * Récupère un patient via son identifiant unique.
     * @param id Identifiant du patient
     * @return Les informations du patient
     */
    // CORRECTION ICI : Ajout de "/api" pour correspondre au PatientController
    @GetMapping("/api/patients/{id}")
    Patient getPatientById(@PathVariable("id") Long id);
}