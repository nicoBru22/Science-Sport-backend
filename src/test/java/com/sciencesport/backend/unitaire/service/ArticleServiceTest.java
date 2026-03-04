package com.sciencesport.backend.unitaire.service;

import com.sciencesport.backend.model.Article;
import com.sciencesport.backend.repository.ArticleRepository;
import com.sciencesport.backend.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void getArticleByIdTest() {
        Article articleTest = new Article();
        articleTest.setId("1");
        articleTest.setTitre("Test Article");

        when(articleRepository.findById("1"))
                .thenReturn(Optional.of(articleTest));

        Article result = articleService.getArticleById("1");

        assertNotNull(result);
        assertEquals("Test Article", result.getTitre());

        verify(articleRepository, times(1)).findById("1");
    }

    @Test
    void getAllArticlesTest() {
        Article articleTest1 = new Article();
        articleTest1.setId("1");

        Article articleTest2 = new Article();
        articleTest2.setId("2");

        when(articleRepository.findAll()).thenReturn(Arrays.asList(articleTest1, articleTest2));

        List<Article> result = articleService.getAllArticles();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void deleteArticleById_shouldCallRepositoryDeleteById() {
        String id = "123";

        articleService.deleteArticleById(id);

        verify(articleRepository, times(1)).deleteById(id);
    }

    @Test
    void getThreeLastArticles_shouldReturnThreeMostRecentArticles() {
        ArticleService spyService = Mockito.spy(articleService);

        Article a1 = new Article();
        a1.setDateModification(new Date(1000));

        Article a2 = new Article();
        a2.setDateModification(new Date(2000));

        Article a3 = new Article();
        a3.setDateModification(new Date(3000));

        Article a4 = new Article();
        a4.setDateModification(new Date(4000));

        List<Article> articles = List.of(a1, a2, a3, a4);

        doReturn(articles).when(spyService).getAllArticles();

        List<Article> result = spyService.getThreeLastArticles();

        assertEquals(3, result.size());
        assertEquals(a4, result.get(0));
        assertEquals(a3, result.get(1));
        assertEquals(a2, result.get(2));
    }

    @Test
    void getThreeLastArticles_shouldReturnAllIfLessThanThree() {
        ArticleService spyService = Mockito.spy(articleService);

        Article a1 = new Article();
        a1.setDateModification(new Date(1000));

        Article a2 = new Article();
        a2.setDateModification(new Date(2000));

        List<Article> articles = List.of(a1, a2);

        doReturn(articles).when(spyService).getAllArticles();

        List<Article> result = spyService.getThreeLastArticles();

        assertEquals(2, result.size());
    }

    @Test
    void addArticle_shouldSaveArticleAndSetDates() {
        Article article = new Article();

        when(articleRepository.save(any(Article.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Article saved = articleService.addArticle(article);

        assertNotNull(saved.getDateCreation());
        assertNotNull(saved.getDateModification());

        verify(articleRepository, times(1)).save(article);
    }

    @Test
    void addArticle_shouldThrowExceptionIfArticleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            articleService.addArticle(null);
        });

        verify(articleRepository, never()).save(any());
    }


}