package com.dbfp.footprint.dto.review;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewListDto {
    private Long memberId;
    private Long reviewId;
    private String title;
    private String previewImageUrl;

    public ReviewListDto(Long reviewId, Long memberId, String title, String previewImageUrl) {
        this.memberId = memberId;
        this.reviewId = reviewId;
        this.title = title;
        this.previewImageUrl = previewImageUrl;
    }
}
