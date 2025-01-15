package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmo.greenThumb.dto.ArticleDTO;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.services.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createArticle() throws Exception {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");
        article.setText("Test pour article.");
        article.setDate(new Date());

        Mockito.when(articleService.createArticle(Mockito.any(Article.class))).thenReturn(article);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Article"))
                .andExpect(jsonPath("$.text").value("Test pour article."));
    }

    @Test
    void getAllArticles() throws Exception {
        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Test Article 1");
        article1.setText("article de test 1");
        article1.setDate(new Date());

        Article article2 = new Article();
        article2.setId(2L);
        article2.setTitle("Test Article 2");
        article2.setText("artoicle de test 2");
        article2.setDate(new Date());

        List<ArticleDTO> articles = null;// TODO: switch article dto

        Mockito.when(articleService.getAllArticles("")).thenReturn(articles);//TODO:update article

        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Article 1"))
                .andExpect(jsonPath("$[0].text").value("article de test 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Test Article 2"))
                .andExpect(jsonPath("$[1].text").value("artoicle de test 2"));
    }

    @Test
    void getArticleById() throws Exception {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");
        article.setText("Test test test article.");
        article.setDate(new Date());

        Mockito.when(articleService.getArticleById(1L)).thenReturn(article);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Article"))
                .andExpect(jsonPath("$.text").value("Test test test article."));
    }


    @Test
    void deleteArticle() throws Exception {
        Mockito.doNothing().when(articleService).deleteArticle(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}