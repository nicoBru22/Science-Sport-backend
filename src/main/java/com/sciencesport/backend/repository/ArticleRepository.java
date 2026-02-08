package com.sciencesport.backend.repository;

import com.sciencesport.backend.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ArticleRepository extends MongoRepository<Article, String> {

}
