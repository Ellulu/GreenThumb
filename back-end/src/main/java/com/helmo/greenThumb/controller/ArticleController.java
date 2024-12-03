package com.helmo.greenThumb.controller;


import com.helmo.greenThumb.dto.ArticleDTO;
import com.helmo.greenThumb.dto.LikeRequestDTO;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.services.ArticleService;
import com.helmo.greenThumb.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @PostMapping
    public Article createArticle(@RequestBody Article article) {

        System.out.println("creating article :"+article.toString());
        return articleService.createArticle(article);
    }

    @GetMapping
    public List<ArticleDTO> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
    @PostMapping("/{articleId}/like")
    public ResponseEntity<String> updateLikeOrDislike(
            @PathVariable Long articleId,
            @RequestBody LikeRequestDTO likeRequest) {
        System.out.println("articleId: " + articleId);
        System.out.println("likeRequest reçu : " + likeRequest);

        if (likeRequest == null) {
            return ResponseEntity.badRequest().body("Corps de la requête manquant ou mal formé.");
        }

        try {
            articleService.likeOrDislikeArticle(articleId, likeRequest.userId(), likeRequest.isLike());
            return ResponseEntity.ok("Réaction mise à jour avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

