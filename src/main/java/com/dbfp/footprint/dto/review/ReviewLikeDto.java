package com.dbfp.footprint.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLikeDto {
    @Schema(name = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(name = "작성자 ID", example = "1")
    private Long memberId;

    public ReviewLikeDto(Long reviewId, Long memberId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
    }
}
