package com.medilabo.client_ui.proxies;

import com.medilabo.client_ui.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Proxy Feign pour la communication avec le microservice de gestion des informations patients.
 */
@FeignClient(name = "gateway-patient", url = "${gateway.url:localhost:9004}")
public interface MicroservicePatientProxy {

    /**
     * Récupère la liste de tous les patients enregistrés.
     *
     * @return           Une liste d'objets PatientDto.
     */
    @GetMapping(value = "/api/patients")
    List<PatientDto> patients();

    /**
     * Récupère les informations détaillées d'un patient par son identifiant.
     *
     * @param id         L'identifiant unique du patient.
     * @return           L'objet PatientDto correspondant.
     */
    @GetMapping(value = "/api/patients/{id}")
    PatientDto getPatient(@PathVariable("id") long id);

    /**
     * Met à jour les informations personnelles d'un patient existant.
     *
     * @param id         L'identifiant du patient à modifier.
     * @param patient    Les nouvelles informations du patient.
     * @return           L'objet PatientDto mis à jour.
     */
    @PutMapping(value = "/api/patients/{id}")
    PatientDto updatePatient(@PathVariable("id") long id,
                             @RequestBody PatientDto patient);
}