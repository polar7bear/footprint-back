package com.dbfp.footprint.dto.review;

import com.dbfp.footprint.domain.review.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record ImageDto(
        Long imageId,
        String imageUrl
) {
    public static ImageDto of(Image image){
        return ImageDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
