package com.helmo.greenThumb.dto;

import java.util.List;

public record ArticleDTO(
        Long id,
        String title,
        String text,
        AuthorDTO author,
        String date,
        List<String> files,
        RatingDTO rating,
        List<com.helmo.greenThumb.model.Comment> comments
) {}