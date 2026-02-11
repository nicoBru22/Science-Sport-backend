package com.sciencesport.backend.service;

import com.sciencesport.backend.model.Article;

import java.util.List;

public interface IntArticleService {

    public List<Article> getAllArticles();
    public Article getArticleById(String id);
    public void deleteArticleById(String id);
    public Article addArticle(Article article);
    public List<Article> getThreeLastArticles();

}
