package com.helmo.greenThumb.utils;

import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.UserRecord;
import com.helmo.greenThumb.dto.ArticleDTO;
import com.helmo.greenThumb.dto.AuthorDTO;
import com.helmo.greenThumb.dto.RatingDTO;
import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.model.Rating;
import com.helmo.greenThumb.model.User;

import java.util.List;
import java.util.Set;

public class DTOConverter {
    public DTOConverter(){

    }
    public ArticleDTO toArticleDTO(Article article, UserRecord author,String uid){
        return new ArticleDTO(article.getId(),article.getTitle(),article.getText(),toAuthorDTO(author),article.getDate().toString(),article.getFiles(),toRatingDTO(article.getRatings(),uid),article.getComments());
    }
    public AuthorDTO toAuthorDTO(UserRecord author){
        return new AuthorDTO(author.getDisplayName(),author.getPhotoUrl(), author.getUid());
    }
    public RatingDTO toRatingDTO(Set<Rating> ratings, String authorUID){
        int likes = 0;
        int dislikes = 0;
        boolean likedByAuthor = false;
        boolean dislikedByAuthor = false;
        System.out.println("rating uid:"+authorUID);
        for (var r : ratings) {
            if(r.isLiked()){
                likes++;
                if (authorUID.equals(r.getUser().getUid())){
                    likedByAuthor = true;
                }
            }else{
                dislikes++;
                if (authorUID.equals(r.getUser().getUid())){
                    dislikedByAuthor = true;
                }
            }
        }
        return new RatingDTO(likes,dislikes,likedByAuthor,dislikedByAuthor);
    }
}
