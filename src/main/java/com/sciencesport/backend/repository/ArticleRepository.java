package com.sciencesport.backend.repository;

import com.sciencesport.backend.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Interface de dépôt (Repository) pour la gestion des articles en base de données MongoDB.
 * Fournit des opérations CRUD de base ainsi que des méthodes de requêtage personnalisées
 * pour l'entité {@link Article}.
 * 
 * @see Article
 */
public interface ArticleRepository extends MongoRepository<Article, String> {

}
