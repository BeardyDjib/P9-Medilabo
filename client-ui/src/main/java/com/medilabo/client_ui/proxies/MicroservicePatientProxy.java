package com.medilabo.client_ui.proxies;

import com.medilabo.client_ui.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "gateway-patient", url = "localhost:9004")
public interface MicroservicePatientProxy {

    // 1. Récupérer tous les patients
    @GetMapping(value = "/api/patients")
    List<PatientDto> patients(@RequestHeader("Authorization") String authHeader);

    // 2. Récupérer un seul patient par son ID
    @GetMapping(value = "/api/patients/{id}")
    PatientDto getPatient(@PathVariable("id") long id, @RequestHeader("Authorization") String authHeader);

    // 3. Mettre à jour un patient
    @PutMapping(value = "/api/patients/{id}")
    PatientDto updatePatient(@PathVariable("id") long id,
                             @RequestBody PatientDto patient,
                             @RequestHeader("Authorization") String authHeader);
}