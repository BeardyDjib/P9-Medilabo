package com.medilabo.note_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id; // L'ID technique Mongo

    private Integer patId; // L'ID du patient (1, 2, 3 ou 4)
    private String patient; // Le nom
    private String note;    // Le contenu du médecin

    private LocalDateTime date; // Champ date
}