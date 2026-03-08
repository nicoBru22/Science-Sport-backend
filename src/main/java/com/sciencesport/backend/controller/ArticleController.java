package com.sciencesport.backend.controller;

import com.sciencesport.backend.model.Article;
import com.sciencesport.backend.service.IntArticleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations liées aux articles.
 * <p>
 * Fournit des endpoints pour :
 * <ul>
 *     <li>Récupérer tous les articles</li>
 *     <li>Récupérer les trois derniers articles</li>
 *     <li>Récupérer un article par son identifiant</li>
 *     <li>Ajouter un nouvel article</li>
 *     <li>Mettre à jour un article existant</li>
 *     <li>Supprimer un article par son identifiant</li>
 * </ul>
 * <p>
 * Le contrôleur utilise {@link IntArticleService} pour déléguer la logique métier
 * et {@link Logger} pour les logs.
 * </p>
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private Logger logger = LogManager.getLogger(ArticleController.class);

    private final IntArticleService intArticleService;

    /**
     * Constructeur du contrôleur avec injection du service métier.
     *
     * @param intArticleService le service pour la gestion des articles
     */
    public ArticleController(IntArticleService intArticleService) {
        this.intArticleService = intArticleService;
    }

    /**
     * Récupère tous les articles.
     *
     * @return une {@link ResponseEntity} contenant la liste des articles et le statut HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Article>> findAll() {
        logger.info("Entrée dans le controller pour récupération de la liste des articles.");
        List<Article> listArticles = intArticleService.getAllArticles();
        logger.info("Retourne la liste des articles.");
        return new ResponseEntity<>(listArticles, HttpStatus.OK);
    }

    /**
     * Récupère les trois derniers articles selon la date de modification.
     *
     * @return une {@link ResponseEntity} contenant la liste des trois derniers articles et le statut HTTP 200
     */
    @GetMapping("/troisDerniersArticles")
    public ResponseEntity<List<Article>> getTroisDerniersArticles() {
        logger.info("Entrée dans le Controller pour récupérer les 3 derniers articles.");
        List<Article> listTroisDerniersArticles = intArticleService.getThreeLastArticles();
        logger.info("Retourne la liste des derniers articles.");
        return ResponseEntity.ok(listTroisDerniersArticles);
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id l'identifiant de l'article
     * @return une {@link ResponseEntity} contenant l'article et le statut HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Article> findById(@PathVariable String id) {
        logger.info("Entrée dans le Controller pour récupérer un article par son Id.");
        Article articleFinded = intArticleService.getArticleById(id);
        logger.info("Retourne l'article par son Id.");
        return ResponseEntity.ok(articleFinded);
    }

    /**
     * Ajoute un nouvel article.
     *
     * @param article l'article à créer
     * @return une {@link ResponseEntity} contenant l'article créé et le statut HTTP 201
     */
    @PostMapping
    public ResponseEntity<Article> save(@RequestBody Article article) {
        logger.info("Entrée dans le Controller pour ajouter un nouvel article : {}", article);
        Article createdArticle = intArticleService.addArticle(article);
        logger.info("Ajout de l'article réussi avec l'ID : {}", createdArticle.getId());
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    /**
     * Supprime un article par son identifiant.
     *
     * @param id l'identifiant de l'article à supprimer
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        intArticleService.deleteArticleById(id);
    }

    /**
     * Met à jour un article existant.
     *
     * @param id      l'identifiant de l'article à mettre à jour (pris depuis l'URL)
     * @param article l'article avec les nouvelles valeurs
     * @return une {@link ResponseEntity} contenant l'article mis à jour et le statut HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<Article> update(
            @PathVariable String id,
            @RequestBody Article article) {
        logger.info("Entrée dans le Controller pour modifier l'article : {}", id);
        article.setId(id); // sécurité : on force l'id depuis l'URL
        Article updatedArticle = intArticleService.updateArticle(article);
        logger.info("Modification de l'article réussie");
        return ResponseEntity.ok(updatedArticle);
    }
}