package com.medilabo.assessment_service.proxies;

import com.medilabo.assessment_service.model.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * Proxy Feign pour la communication avec le microservice de gestion des notes.
 */
@FeignClient(name = "note-service", url = "${note-service.url:http://localhost:9002}")
public interface NoteProxy {

    /**
     * Récupère l'historique complet des notes d'un patient.
     *
     * @param patId L'identifiant du patient.
     * @return      Une liste d'objets Note.
     */
    @GetMapping("/notes/patient/{patId}")
    List<Note> getNotesByPatientId(@PathVariable("patId") Long patId);
}