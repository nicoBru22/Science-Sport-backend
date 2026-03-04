package com.sciencesport.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Représente un article scientifique ou informatif stocké en base MongoDB.
 *
 * <p>
 * Cette classe est mappée sur la collection "articles".
 * Un article est composé :
 * </p>
 * <ul>
 *     <li>d’un titre et d’une catégorie</li>
 *     <li>d’une introduction</li>
 *     <li>d’une liste dynamique de sections</li>
 *     <li>d’une conclusion</li>
 *     <li>de références scientifiques</li>
 *     <li>d’un lien vers un article externe</li>
 *     <li>d’une image encodée en Base64</li>
 *     <li>de dates de création et de modification</li>
 * </ul>
 *
 * <p>
 * L’annotation {@code @Data} de Lombok génère automatiquement :
 * getters, setters, toString(), equals() et hashCode().
 * </p>
 *
 * <p>
 * L’annotation {@code @Document} indique que cette classe correspond
 * à un document MongoDB.
 * </p>
 */
@Data
@Document(collection = "articles")
public class Article {

    /**
     * Identifiant unique de l’article généré par MongoDB.
     */
    @Id
    private String id;

    /**
     * Titre principal de l’article.
     */
    private String titre;

    /**
     * Catégorie de l’article (ex : Nutrition, Entraînement, Récupération).
     */
    private String categorie;

    /**
     * Introduction de l’article.
     * Partie affichée au début du contenu.
     */
    private Section introduction;

    /**
     * Liste dynamique des différentes sections de l’article.
     * Chaque section contient un sous-titre et un texte.
     */
    private List<Section> sections;

    /**
     * Conclusion de l’article.
     */
    private Section conclusion;

    /**
     * Liste des références scientifiques utilisées pour rédiger l’article.
     */
    private List<String> references;

    /**
     * Lien externe vers l’article scientifique original ou source principale.
     */
    private String lienArticle;

    /**
     * Image principale de l’article encodée au format Base64.
     * Permet le stockage direct dans la base sans système de fichiers.
     */
    private String imageBase64;

    /**
     * Date de création de l’article.
     */
    private Date dateCreation;

    /**
     * Date de dernière modification de l’article.
     */
    private Date dateModification;

    /**
     * Représente une section d’un article.
     *
     * <p>
     * Utilisée pour :
     * </p>
     * <ul>
     *     <li>l’introduction</li>
     *     <li>les sections intermédiaires</li>
     *     <li>la conclusion</li>
     * </ul>
     *
     * <p>
     * Cette classe interne est statique car elle ne dépend pas
     * d’une instance spécifique d’Article.
     * </p>
     */
    @Data
    public static class Section {

        /**
         * Sous-titre de la section.
         */
        private String sousTitre;

        /**
         * Contenu textuel principal de la section.
         */
        private String texte;
    }
}