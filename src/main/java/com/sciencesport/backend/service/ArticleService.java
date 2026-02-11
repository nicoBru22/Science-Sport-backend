package com.sciencesport.backend.service;

import com.sciencesport.backend.model.Article;
import com.sciencesport.backend.repository.ArticleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService implements IntArticleService{

    private Logger logger =  LogManager.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        logger.info("Entrée dans le service pour récupérer la liste des articles");
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            logger.info("Le liste des articles est vide");
            return articles;
        } else {
            logger.info("La liste contient {} articles", articles.size());
            return articles;
        }
    }

    public Article getArticleById(String id) {
        logger.info("Entrée dans le Service pour récupérer un article par son Id : {}", id);
        Article articleFinded = articleRepository.findById(id).get();
        logger.info("L'article retourné avec succès : {}", articleFinded);
        return articleFinded;
    }

    public void deleteArticleById(String id) {
        logger.info("Entrée dans le Service pour supprimer un article par son Id.");
        articleRepository.deleteById(id);
        logger.info("Suppression de l'article réussi");
    }

    public List<Article> getThreeLastArticles() {
        logger.info("Entrée dans le service pour récupérer la liste des 3 derniers articles.");
        List<Article> listArticle = getAllArticles();

        // On trie par date décroissante et on prend les 3 premiers
        List<Article> troisDerniersArticles = listArticle.stream()
                .sorted(Comparator.comparing(Article::getDateModification).reversed()) // tri décroissant
                .limit(3) // on prend les 3 premiers
                .collect(Collectors.toList());
        logger.info("le nombre d'article récupéré : {}", troisDerniersArticles.size());
        return troisDerniersArticles;
    }


    public Article addArticle(Article article) {
        logger.info("Entrée dans le Service pour ajouter un nouvel article.");
        if (article == null) {
            logger.info("L'article ne peut pas être null : {}", article);
            throw new IllegalArgumentException("L'article ne peut pas être null");
        }
        article.setDateCreation(new Date());
        article.setDateModification(new Date());

        Article savedArticle = articleRepository.save(article);
        logger.info("Création de l'article réussie.");

        return savedArticle;
    }
}