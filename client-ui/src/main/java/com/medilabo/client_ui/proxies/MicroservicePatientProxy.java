package com.medilabo.client_ui.proxies;

import com.medilabo.client_ui.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

// On vise la GATEWAY (Port 9004)
@FeignClient(name = "gateway-service", url = "localhost:9004")
public interface MicroservicePatientProxy {

    @GetMapping(value = "/api/patients")
    List<PatientDto> patients(@RequestHeader("Authorization") String authHeader);
}