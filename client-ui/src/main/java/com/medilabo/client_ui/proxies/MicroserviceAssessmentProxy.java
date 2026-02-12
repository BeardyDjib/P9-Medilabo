package com.medilabo.client_ui.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

// On passe par la Gateway (9004)
@FeignClient(name = "gateway-assessment", url = "localhost:9004")
public interface MicroserviceAssessmentProxy {

    @GetMapping("/assess/{id}")
    String getAssessment(@PathVariable("id") long id, @RequestHeader("Authorization") String authHeader);
}