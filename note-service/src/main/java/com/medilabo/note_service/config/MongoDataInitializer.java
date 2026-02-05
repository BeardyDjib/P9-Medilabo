package com.medilabo.note_service.config;

import com.medilabo.note_service.model.Note;
import com.medilabo.note_service.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime; // <-- Import nécessaire ajouté

@Configuration
public class MongoDataInitializer {

    @Bean
    public CommandLineRunner initData(NoteRepository noteRepository) {
        return args -> {
            noteRepository.deleteAll(); // On vide avant de remplir

            // On définit une date par défaut pour l'initialisation
            LocalDateTime now = LocalDateTime.now();

            // TestNone (ID 1)
            // On ajoute 'now' comme 5ème argument
            noteRepository.save(new Note(null, 1, "TestNone", "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé", now));

            // TestBorderline (ID 2)
            noteRepository.save(new Note(null, 2, "TestBorderline", "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement", now));
            noteRepository.save(new Note(null, 2, "TestBorderline", "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale", now));

            // TestInDanger (ID 3)
            noteRepository.save(new Note(null, 3, "TestInDanger", "Le patient déclare qu'il fume depuis peu", now));
            noteRepository.save(new Note(null, 3, "TestInDanger", "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d'apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé", now));

            // TestEarlyOnset (ID 4)
            noteRepository.save(new Note(null, 4, "TestEarlyOnset", "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d'être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments", now));
            noteRepository.save(new Note(null, 4, "TestEarlyOnset", "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", now));
            noteRepository.save(new Note(null, 4, "TestEarlyOnset", "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé", now));
            noteRepository.save(new Note(null, 4, "TestEarlyOnset", "Taille, Poids, Cholestérol, Vertige et Réaction", now));

            System.out.println("--- DONNÉES MONGODB CHARGÉES AVEC SUCCÈS ---");
        };
    }
}