package com.sciencesport.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BackendApplication.class);

		// 1. Charger le .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// 2. Transférer les variables dans une Map
		Map<String, Object> envVariables = new HashMap<>();
		for (DotenvEntry entry : dotenv.entries()) {
			envVariables.put(entry.getKey(), entry.getValue());
		}

		// 3. LOG DE VÉRIFICATION
		String mongoUri = (String) envVariables.get("MONGODB_URI");
		if (mongoUri != null && !mongoUri.isEmpty()) {
			// On affiche seulement le début pour vérifier que ce n'est pas vide ou null
			System.out.println("✅ [DEBUG] MONGODB_URI chargée avec succès. Début : " + mongoUri.substring(0, 20) + "...");
		} else {
			System.err.println("❌ [DEBUG] MONGODB_URI est INTROUVABLE dans le fichier .env !");
		}

		// 4. Ajouter cette Map aux propriétés de l'application
		app.setDefaultProperties(envVariables);

		app.run(args);
	}

}
