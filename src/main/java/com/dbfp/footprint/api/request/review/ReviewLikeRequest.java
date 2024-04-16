package com.dbfp.footprint.api.request.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLikeRequest {
    @Schema(name = "reviewId", example = "1")
    private Long reviewId;

    @Schema(name = "memberId", example = "1")
    private Long memberId;

    public ReviewLikeRequest(Long reviewId, Long memberId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
    }
}
