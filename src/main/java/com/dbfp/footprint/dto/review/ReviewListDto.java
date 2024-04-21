package com.dbfp.footprint.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewListDto {
    @Schema(name = "memberId", example = "1")
    private Long memberId;

    @Schema(name = "nickname", example = "닉네임123")
    private String nickname;

    @Schema(name = "reviewId", example = "1")
    private Long reviewId;

    @Schema(name = "title", example = "부산 1박 2일 여행")
    private String title;

    @Schema(name = "createdAt", example = "2024-04-11T17:18:53.457879")
    private LocalDateTime createdAt;

    @Schema(name = "좋아요 개수", example = "1")
    private Integer likes;


    @Schema(name = "previewImageUrl", example = "https://dfbf-footprint.s3.ap-northeast-2.amazonaws.com/472684f1-3618-47ae-8cc4-493179ae85a8_1.jpg")
    private String previewImageUrl;

    public ReviewListDto(Long reviewId, Long memberId, String nickname, String title,
                         LocalDateTime createdAt, Integer likes, String previewImageUrl) {
        this.memberId = memberId;
        this.reviewId = reviewId;
        this.nickname = nickname; //
        this.title = title;
        this.createdAt = createdAt;
        this.likes = likes;
        this.previewImageUrl = previewImageUrl;
    }
}
