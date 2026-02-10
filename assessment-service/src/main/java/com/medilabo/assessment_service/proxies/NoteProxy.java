package com.medilabo.assessment_service.proxies;

import com.medilabo.assessment_service.model.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Proxy Feign pour la communication avec le microservice Note-Service.
 */
@FeignClient(name = "note-service", url = "localhost:9002")
public interface NoteProxy {

    /**
     * Récupère la liste complète des notes associées à un patient.
     * @param patId Identifiant du patient
     * @return Liste des notes trouvées
     */
    @GetMapping("/notes/patient/{patId}")
    List<Note> getNotesByPatientId(@PathVariable("patId") Long patId);
}