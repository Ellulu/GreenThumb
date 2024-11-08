package com.helmo.greenThumb.controller;


import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @GetMapping
    public List<Article> getAllArticles() {
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
    @PutMapping("/{articleId}/like")
    public ResponseEntity<String> updateLikeOrDislike(
            @PathVariable Long articleId,
            @RequestParam Long userId,
            @RequestParam boolean isLike) {

        try {
            articleService.likeOrDislikeArticle(articleId, userId, isLike);
            return ResponseEntity.ok("Reaction updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

