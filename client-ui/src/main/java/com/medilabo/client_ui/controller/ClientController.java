package com.medilabo.client_ui.controller;

import com.medilabo.client_ui.dto.PatientDto;
import com.medilabo.client_ui.proxies.MicroservicePatientProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ClientController {

    private final MicroservicePatientProxy patientProxy;

    public ClientController(MicroservicePatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @RequestMapping("/")
    public String accueil(Model model){
        // Basic Auth pour passer la sécurité du Back (user:password)
        String authHeader = "Basic dXNlcjpwYXNzd29yZA==";

        // Récupération des patients via le Proxy
        List<PatientDto> patients = patientProxy.patients(authHeader);

        // Envoi à la vue
        model.addAttribute("patients", patients);

        return "accueil";
    }
}