package com.medilabo.client_ui.proxies;

import com.medilabo.client_ui.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;

/**
 * Proxy Feign pour la communication avec le microservice de gestion des notes médicales.
 */
@FeignClient(name = "gateway-note", url = "${gateway.url:localhost:9004}")
public interface MicroserviceNoteProxy {

    /**
     * Récupère l'historique des notes médicales pour un patient donné.
     *
     * @param patId       L'identifiant du patient.
     * @param authHeader  L'en-tête d'authentification Basic Auth.
     * @return            La liste des notes associées au patient.
     */
    @GetMapping("/notes/patient/{patId}")
    List<NoteDto> getNotesByPatientId(@PathVariable("patId") int patId, @RequestHeader("Authorization") String authHeader);

    /**
     * Ajoute une nouvelle note médicale dans l'historique.
     *
     * @param noteDto     Les données de la note à créer.
     * @param authHeader  L'en-tête d'authentification Basic Auth.
     * @return            La note créée.
     */
    @PostMapping("/notes")
    NoteDto addNote(@RequestBody NoteDto noteDto, @RequestHeader("Authorization") String authHeader);
}