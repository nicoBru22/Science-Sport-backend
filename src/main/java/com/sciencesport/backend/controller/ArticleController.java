package com.sciencesport.backend.controller;

import com.sciencesport.backend.model.Article;
import com.sciencesport.backend.repository.ArticleRepository;
import com.sciencesport.backend.service.IntArticleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private Logger logger = LogManager.getLogger(ArticleController.class);

    private final IntArticleService intArticleService;

    public ArticleController(IntArticleService intArticleService) {
        this.intArticleService = intArticleService;
    }

    @GetMapping
    public ResponseEntity<List<Article>> findAll() {
        logger.info("Entrée dans le controller pour récupération de la liste des articles.");
        List<Article> listArticles = intArticleService.getAllArticles();
        logger.info("Retourne la liste des articles.");
        return new ResponseEntity<>(listArticles, HttpStatus.OK) ;
    }

    @GetMapping("/troisDerniersArticles")
    public ResponseEntity<List<Article>> getTroisDerniersArticles() {
        logger.info("Entrée dans le Controller pour récupérer les 3 derniers articles.");
        List<Article> listTroisDerniersArticles = intArticleService.getThreeLastArticles();
        logger.info("Retourne la liste des derniers articles.");
        return ResponseEntity.ok(listTroisDerniersArticles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> findById(@PathVariable String id) {
        logger.info("Entrée dans le Controller pour récupérer un article par son Id.");
        Article articleFinded = intArticleService.getArticleById(id);
        logger.info("Retourne la article par son Id.");
        return ResponseEntity.ok(articleFinded);
    }



    @PostMapping
    public ResponseEntity<Article> save(@RequestBody Article article) {
        logger.info("Entrée dans le Controller pour ajouter un nouvel article : {}", article);
        Article createdArticle = intArticleService.addArticle(article);
        logger.info("Ajout de l'article réussi avec l'ID : {}", createdArticle.getId());
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        intArticleService.deleteArticleById(id);
    }

/*
    @PostMapping
    public Article save(@RequestBody Article article) {

    }
*/
}
