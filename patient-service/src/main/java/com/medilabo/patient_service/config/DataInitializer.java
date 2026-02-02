package com.medilabo.patient_service.config;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            // Insertion des données de test fournies par le client
            patientRepository.save(new Patient(null, "Test", "TestNone", LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333"));
            patientRepository.save(new Patient(null, "Test", "TestBorderline", LocalDate.of(1945, 6, 24), "M", "2 High St", "200-333-4444"));
            patientRepository.save(new Patient(null, "Test", "TestinDanger", LocalDate.of(2004, 6, 18), "M", "3 Club Road", "300-444-5555"));
            patientRepository.save(new Patient(null, "Test", "TestEarlyOnset", LocalDate.of(2002, 6, 28), "F", "4 Valley Dr", "400-555-6666"));

            System.out.println(">>> SUCCESS : Les 4 dossiers patients de test sont archivés en base SQL !");
        };
    }
}