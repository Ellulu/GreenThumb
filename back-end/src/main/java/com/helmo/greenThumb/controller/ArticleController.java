package com.helmo.greenThumb.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.StorageClient;
import com.helmo.greenThumb.dto.ArticleDTO;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.services.ArticleService;
import com.helmo.greenThumb.services.EmailService;
import com.helmo.greenThumb.services.FirebaseService;
import com.helmo.greenThumb.utils.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private EmailService emailService;
    @PostMapping
    public Article createArticle(@RequestAttribute("firebaseToken") FirebaseToken token,
                                 @RequestParam("article") String articleJson,
                                 @RequestParam(value = "pictures",required = false)List<MultipartFile> pictures) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Article article = objectMapper.readValue(articleJson, Article.class);
            System.out.println(pictures);
            if (!token.getUid().equals(article.getAuthor().getUid())) return new Article();
            if (pictures != null) {
                Bucket bucket = StorageClient.getInstance().bucket("greenthumb-54c99.firebasestorage.app");
                for (var picture : pictures) {
                    if (FileValidator.validateImage(picture)) {

                        String blobName = "article/" + token.getUid() + "/" + new Date().getTime() + "-" + picture.getOriginalFilename();
                        Blob blob = bucket.create(blobName, picture.getInputStream());

                        String encodedBlobName = URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8.toString());
                        String pictureUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucket.getName(), encodedBlobName);
                        System.out.println(pictureUrl);
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

    @GetMapping("/page/{page}")
    public List<ArticleDTO> getArticlesByPage(@RequestAttribute("firebaseToken") FirebaseToken token, @PathVariable int page) {
        System.out.println(">>> Page: " + page);
        return articleService.getArticlesByPage(token.getUid(), page);
    }
    @GetMapping("/offline/page/{page}")
    public List<ArticleDTO> getArticlesByPage(@PathVariable int page) {
        return articleService.getArticlesByPage("", page);
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }


    @DeleteMapping("/{id}")
    public void deleteArticle(
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) return;
        if (!article.getAuthor().getUid().equals(token.getUid())) return;
        articleService.deleteArticle(id);
    }
    @PostMapping("/{articleId}/like")
    public ResponseEntity<String> updateLikeOrDislike(
            @PathVariable Long articleId,
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @RequestBody boolean isLike) {
        try {

            articleService.likeOrDislikeArticle(articleId, token.getUid(), isLike);
            sendMailToAuthor(token.getUid(), isLike);
            return ResponseEntity.ok("Réaction mise à jour avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private void sendMailToAuthor(String uid, boolean isLike) {
        String eMail = firebaseService.getEmailFromUid(uid);
        String message = isLike ?
                "Votre article est populaire! il a été liké."
                :"Votre article a été disliké.";
        emailService.sendEmail(eMail, "[GreenThumb] Nouveau Like sur votre poste", message);
    }

}

