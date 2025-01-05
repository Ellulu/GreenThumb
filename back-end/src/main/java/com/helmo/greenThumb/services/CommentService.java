package com.helmo.greenThumb.services;

import com.google.firebase.auth.UserRecord;
import com.helmo.greenThumb.dto.CommentDTO;
import com.helmo.greenThumb.infrastructures.ArticleRepository;
import com.helmo.greenThumb.infrastructures.CommentRepository;
import com.helmo.greenThumb.infrastructures.UserRepository;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.model.Comment;
import com.helmo.greenThumb.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private FirebaseService firebaseService;
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository,FirebaseService firebaseService) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
    }

    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.stream()
                .map(comment -> {
                    UserRecord firebaseUser = firebaseService.getUserByUid(comment.getUser().getUid());
                    return new CommentDTO(
                            comment.getId(),
                            comment.getText(),
                            firebaseUser.getDisplayName(),
                            firebaseUser.getPhotoUrl(),
                            comment.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
    public Comment createComment(Long articleId, String userId, String content) {
        // Recherche de l'article
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));

        // Recherche de l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Création du commentaire
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setUser(user);
        comment.setText(content);
        comment.setCreatedAt(LocalDateTime.now());

        // Sauvegarde du commentaire
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
