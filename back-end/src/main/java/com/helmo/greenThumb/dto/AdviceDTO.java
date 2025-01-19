package com.helmo.greenThumb.dto;

import java.util.List;

public record AdviceDTO(
        Long id,
        String title,
        String text,
        AuthorDTO author,
        String date,
        List<String> files
) {}