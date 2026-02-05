package com.medilabo.client_ui.proxies;

import com.medilabo.client_ui.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// On vise la GATEWAY (Port 9004) comme pour les patients
// Note : le "name" dans FeignClient doit être unique par interface, d'où "gateway-note"
@FeignClient(name = "gateway-note", url = "localhost:9004")
public interface MicroserviceNoteProxy {

    // Appel vers l'endpoint: GET /notes/patient/{patId}
    @GetMapping(value = "/notes/patient/{patId}")
    List<NoteDto> getNotesByPatientId(@PathVariable("patId") Integer patId,
                                      @RequestHeader("Authorization") String authHeader);
    @PostMapping(value = "/notes")
    NoteDto addNote(@RequestBody NoteDto note, @RequestHeader("Authorization") String authHeader);
}