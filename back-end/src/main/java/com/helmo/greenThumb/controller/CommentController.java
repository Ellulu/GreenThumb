package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.dto.CommentDTO;
import com.helmo.greenThumb.model.Comment;
import com.helmo.greenThumb.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long articleId) {
        List<CommentDTO> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<Comment> createComment(
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @PathVariable Long articleId,
            @RequestBody CommentRequest commentRequest) {
        Comment comment = commentService.createComment(
                articleId,
                token.getUid(),
                commentRequest.getContent()
        );
        return ResponseEntity.status(201).body(comment);
    }

    @DeleteMapping("/{commentId}/comments")
    public ResponseEntity<Void> deleteComment(
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @PathVariable Long commentId) {
        if(!commentService.isCommentOwner(commentId,token.getUid())) {
            return ResponseEntity.status(403).build();
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

class CommentRequest {
    private String userId;
    private String content;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
