package com.dbfp.footprint.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewListDto {
    @Schema(name = "작성자 ID", example = "1")
    private Long memberId;

    @Schema(name = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(name = "리뷰 제목", example = "부산 1박 2일 여행")
    private String title;

    @Schema(name = "리뷰 썸네일 이미지 URL", example = "https://dfbf-footprint.s3.ap-northeast-2.amazonaws.com/472684f1-3618-47ae-8cc4-493179ae85a8_1.jpg")
    private String previewImageUrl;

    public ReviewListDto(Long reviewId, Long memberId, String title, String previewImageUrl) {
        this.memberId = memberId;
        this.reviewId = reviewId;
        this.title = title;
        this.previewImageUrl = previewImageUrl;
    }
}
