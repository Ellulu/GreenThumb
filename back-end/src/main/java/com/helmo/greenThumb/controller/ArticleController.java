package com.helmo.greenThumb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.StorageClient;
import com.helmo.greenThumb.dto.ArticleDTO;
import com.helmo.greenThumb.dto.LikeRequestDTO;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.model.Plant;
import com.helmo.greenThumb.services.ArticleService;
import com.helmo.greenThumb.services.FirebaseService;
import com.helmo.greenThumb.utils.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @PostMapping
    public Article createArticle(@RequestAttribute("firebaseToken") FirebaseToken token,
                                 @RequestParam("article") String articleJson,
                                 @RequestParam(value = "pictures",required = false)List<MultipartFile> pictures) {
        try {
            System.out.println(pictures.size());
            ObjectMapper objectMapper = new ObjectMapper();
            Article article = objectMapper.readValue(articleJson, Article.class);
            
            if (!token.getUid().equals(article.getAuthor().getUid())) return new Article();

            if (pictures != null) {
                Bucket bucket = StorageClient.getInstance().bucket("greenthumb-54c99.firebasestorage.app");
                for (var picture : pictures) {
                    if (FileValidator.validateImage(picture)) {
                        String blobName = "article/" + token.getUid() + "/" + new Date().getTime() + "-" + picture.getOriginalFilename();
                        Blob blob = bucket.create(blobName, picture.getInputStream());

                        String pictureUrl = "https://storage.googleapis.com/" + bucket.getName() + "/" + blob.getName();
                        article.getFiles().add(pictureUrl);
                    }
                }
            }

            return articleService.createArticle(article);


        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new Article();
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

