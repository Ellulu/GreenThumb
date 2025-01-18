package com.helmo.greenThumb.dto;

import org.springframework.web.multipart.MultipartFile;

public record PlantDTO(
        String name,
        Double monthlyWaterFrequency,
        String lightLevel,
        String varietyName
) {
}
