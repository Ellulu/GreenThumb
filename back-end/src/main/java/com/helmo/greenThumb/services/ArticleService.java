package com.helmo.greenThumb.services;

import com.helmo.greenThumb.infrastructures.RatingRepository;
import com.helmo.greenThumb.infrastructures.UserRepository;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.infrastructures.ArticleRepository;
import com.helmo.greenThumb.model.Rating;
import com.helmo.greenThumb.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;

    public Article createArticle(Article article) {

        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public void likeOrDislikeArticle(Long articleId, Long userId, boolean isLike) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Vérifier si une réaction existe déjà
        Rating existingRating = ratingRepository.findByArticleAndUser(article, user).orElse(null);

        if (existingRating != null) {
            if (existingRating.isLiked() == isLike) {
                ratingRepository.delete(existingRating);
            } else{
                existingRating.setLiked(isLike);
                ratingRepository.save(existingRating);
            }
        } else {
            // Sinon, on crée une nouvelle réaction
            Rating newRating = new Rating(article, user, isLike);
            ratingRepository.save(newRating);
        }
    }

}
