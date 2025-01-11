package com.helmo.greenThumb.controller;


import com.google.firebase.auth.FirebaseToken;
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

        return articleService.createArticle(article);
    }

    @GetMapping
    public List<ArticleDTO> getUserArticles(@RequestAttribute("firebaseToken") FirebaseToken token) {
        System.out.println("articles  user");
        return articleService.getAllArticles(token.getUid());
    }
    @GetMapping("/all")
    public List<ArticleDTO> getAllArticles() {
        System.out.println("articles no user");
        return articleService.getAllArticles("");
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
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @RequestBody boolean isLike) {
        try {
            articleService.likeOrDislikeArticle(articleId, token.getUid(), isLike);
            return ResponseEntity.ok("Réaction mise à jour avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

