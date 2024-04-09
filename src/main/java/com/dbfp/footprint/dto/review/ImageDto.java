package com.dbfp.footprint.dto.review;

import com.dbfp.footprint.domain.review.Image;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ImageDto(

        @Schema(name = "imageId", example = "1", required = true)
        Long imageId,

        @Schema(name = "imageUrl", example = "https://dfbf-footprint.s3.ap-northeast-2.amazonaws.com/472684f1-3618-47ae-8cc4-493179ae85a8_1.jpg")
        String imageUrl
) {
    public static ImageDto of(Image image){
        return ImageDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
