package com.dbfp.footprint.dto.review.response;

import com.dbfp.footprint.domain.review.Image;
import lombok.Builder;

@Builder
public record ImageResDto(
        Long imageId,
        String imageUrl
) {
    public static ImageResDto of(Image image){
        return ImageResDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
