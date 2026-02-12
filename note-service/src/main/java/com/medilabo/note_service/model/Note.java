package com.medilabo.note_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Entité représentant une note médicale stockée dans MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {

    /** Identifiant technique unique (généré par MongoDB). */
    @Id
    private String id;

    /** Identifiant du patient associé. */
    private Integer patId;

    /** Nom du patient. */
    private String patient;

    /** Contenu de la note rédigé par le médecin. */
    private String note;

    /** Date et heure de création de la note. */
    private LocalDateTime date;
}