package com.sciencesport.backend.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "articles")
public class Article {

    @Id
    private String id;
    private String titre;
    private String categorie;

    private String stIntroArticle;
    private String texteIntroArticle;

    private String st1Article;
    private String texte1Article;

    private String st2Article;
    private String texte2Article;

    private String st3Article;
    private String texte3Article;

    private String stConclusionArticle;
    private String texteConclusionArticle;

    private String imageBase64;
    private Date dateCreation;
    private Date dateModification;
}
