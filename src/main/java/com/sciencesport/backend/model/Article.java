package com.sciencesport.backend.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "articles")
public class Article {

    @Id
    private String id;
    private String titre;
    private String categorie;

    // Introduction dynamique
    private Section introduction;

    // Liste dynamique de sections
    private List<Section> sections;

    // Conclusion dynamique
    private Section conclusion;

    // Références et lien scientifique
    private List<String> references;
    private String lienArticle;

    // Image Base64
    private String imageBase64;

    // Dates
    private Date dateCreation;
    private Date dateModification;

    // Classe interne pour sections / introduction / conclusion
    @Data
    public static class Section {
        private String sousTitre;
        private String texte;
    }
}
