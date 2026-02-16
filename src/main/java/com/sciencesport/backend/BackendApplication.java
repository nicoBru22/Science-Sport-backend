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
		dotenv.entries().forEach(entry -> envVariables.put(entry.getKey(), entry.getValue()));

		// 3. Ajouter cette Map aux propriétés de l'application avant le démarrage
		app.setDefaultProperties(envVariables);

		app.run(args);
	}

}
