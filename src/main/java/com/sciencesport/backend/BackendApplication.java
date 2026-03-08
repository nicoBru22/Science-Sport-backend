package com.sciencesport.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe principale de l'application Spring Boot.
 * <p>
 * Cette classe configure et lance l'application backend pour le projet SportScience.
 * Elle charge les variables d'environnement depuis un fichier `.env` via {@link Dotenv}
 * et les injecte dans Spring Boot comme propriétés par défaut.
 * </p>
 *
 * Fonctionnalités principales :
 * <ul>
 *     <li>Chargement des variables d'environnement depuis `.env`</li>
 *     <li>Injection des variables dans Spring Boot</li>
 *     <li>Vérification et log de la variable MONGODB_URI</li>
 *     <li>Lancement de l'application avec {@link SpringApplication#run(String...)}</li>
 * </ul>
 */
@SpringBootApplication
public class BackendApplication {

	/**
	 * Point d'entrée principal de l'application Spring Boot.
	 * <p>
	 * Cette méthode :
	 * <ol>
	 *     <li>Charge le fichier `.env`</li>
	 *     <li>Transfère les variables dans une Map</li>
	 *     <li>Affiche un log de vérification pour MONGODB_URI</li>
	 *     <li>Injecte les variables dans Spring Boot</li>
	 *     <li>Démarre l'application</li>
	 * </ol>
	 *
	 * @param args arguments de la ligne de commande
	 */
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
			System.out.println("✅ [DEBUG] MONGODB_URI chargée avec succès. Début : " + mongoUri.substring(0, 20) + "...");
		} else {
			System.err.println("❌ [DEBUG] MONGODB_URI est INTROUVABLE dans le fichier .env !");
		}

		// 4. Ajouter cette Map aux propriétés de l'application
		app.setDefaultProperties(envVariables);

		app.run(args);
	}
}