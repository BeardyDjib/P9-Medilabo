package com.medilabo.client_ui.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy Feign pour la communication avec le microservice d'évaluation du risque (Assessment).
 */
@FeignClient(name = "gateway-assessment", url = "${gateway.url:localhost:9004}")
public interface MicroserviceAssessmentProxy {

    /**
     * Récupère le diagnostic du risque de diabète pour un patient spécifique.
     *
     * @param id          L'identifiant du patient.
     * @return            Le niveau de risque calculé sous forme de chaîne de caractères.
     */
    @GetMapping("/assess/{id}")
    String getAssessment(@PathVariable("id") long id);
}