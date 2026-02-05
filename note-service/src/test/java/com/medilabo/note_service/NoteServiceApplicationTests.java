package com.medilabo.note_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoteServiceApplicationTests {

	@Test
	void contextLoads() {
		// Ce test vérifie simplement que le contexte Spring (les beans, la config) se charge sans erreur.
	}

	@Test
	void mainMethodTest() {
		// Ce test force l'appel de la méthode main pour couvrir la classe principale
		// dans le rapport JaCoCo.
		NoteServiceApplication.main(new String[] {});
	}

}