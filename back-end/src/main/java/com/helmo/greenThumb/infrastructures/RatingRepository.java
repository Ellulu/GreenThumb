package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.Article;
import com.helmo.greenThumb.model.Rating;
import com.helmo.greenThumb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.article = :article AND r.user = :user")
    Optional<Rating> findByArticleAndUser(@Param("article") Article article, @Param("user") User user);
}
