package com.medilabo.assessment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modèle représentant une note médicale récupérée depuis le service de notes (MongoDB).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    private String id;
    private String note;
}