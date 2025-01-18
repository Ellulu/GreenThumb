package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.dto.ArticleDTO;
import com.helmo.greenThumb.dto.AuthorDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FirebaseToken firebaseToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(firebaseToken.getUid()).thenReturn("testUser");
    }


    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getUserArticles() throws Exception {
        AuthorDTO author = new AuthorDTO("Test User", "imageUrl", "testUser");
        ArticleDTO article1 = new ArticleDTO(1L, "Test Article 1", "article de test 1", author, "2023-10-10", null, null, null);
        ArticleDTO article2 = new ArticleDTO(2L, "Test Article 2", "article de test 2", author, "2023-10-11", null, null, null);

        List<ArticleDTO> articles = Arrays.asList(article1, article2);

        Mockito.when(articleService.getAllArticles("testUser")).thenReturn(articles);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")
                        .requestAttr("firebaseToken", firebaseToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Article 1"))
                .andExpect(jsonPath("$[0].text").value("article de test 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Test Article 2"))
                .andExpect(jsonPath("$[1].text").value("article de test 2"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getAllArticles() throws Exception {
        AuthorDTO author = new AuthorDTO("Test User", "imageUrl", "testUser");
        ArticleDTO article1 = new ArticleDTO(1L, "Test Article 1", "article de test 1", author, "2023-10-10", null, null, null);
        ArticleDTO article2 = new ArticleDTO(2L, "Test Article 2", "article de test 2", author, "2023-10-11", null, null, null);

        List<ArticleDTO> articles = Arrays.asList(article1, article2);

        Mockito.when(articleService.getAllArticles("")).thenReturn(articles);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Article 1"))
                .andExpect(jsonPath("$[0].text").value("article de test 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Test Article 2"))
                .andExpect(jsonPath("$[1].text").value("article de test 2"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
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




}