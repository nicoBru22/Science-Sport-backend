package com.sciencesport.backend.service;

import com.sciencesport.backend.model.Article;

import java.util.List;

/**
 * Interface définissant les opérations principales pour la gestion des articles.
 * <p>
 * Cette interface est implémentée par {@link ArticleService} et fournit les méthodes pour :
 * <ul>
 *     <li>Récupérer tous les articles ou un article spécifique</li>
 *     <li>Ajouter, mettre à jour ou supprimer un article</li>
 *     <li>Obtenir les trois derniers articles selon leur date de modification</li>
 * </ul>
 */
public interface IntArticleService {

    /**
     * Récupère tous les articles.
     *
     * @return une liste de {@link Article}, vide si aucun article n'est présent
     */
    List<Article> getAllArticles();

    /**
     * Récupère un article par son identifiant.
     *
     * @param id l'identifiant de l'article
     * @return l'article correspondant
     */
    Article getArticleById(String id);

    /**
     * Supprime un article par son identifiant.
     *
     * @param id l'identifiant de l'article à supprimer
     */
    void deleteArticleById(String id);

    /**
     * Ajoute un nouvel article.
     *
     * @param article l'article à ajouter
     * @return l'article ajouté avec les dates de création et modification mises à jour
     */
    Article addArticle(Article article);

    /**
     * Récupère les trois derniers articles selon la date de modification.
     *
     * @return une liste contenant au maximum trois articles les plus récents
     */
    List<Article> getThreeLastArticles();

    /**
     * Met à jour un article existant.
     *
     * @param article l'article à mettre à jour
     * @return l'article mis à jour
     */
    Article updateArticle(Article article);
}
