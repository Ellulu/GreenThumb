package com.helmo.greenThumb.dto;

import java.time.LocalDateTime;

public record CommentDTO(
        Long id,
        String text,
        String username,
        String imageUrl,
        LocalDateTime createdAt
) {}
