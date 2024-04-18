package com.dbfp.footprint.dto.review;

import com.dbfp.footprint.domain.review.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {
    //private Long reviewId;

    @Schema(name = "memberId", example = "1", required = true)
    private Long memberId;

    @Schema(name = "title", example = "1박 2일 부산여행")
    private String title;

    @Schema(name = "content", example = "1")
    private String content;

    @Schema(name = "createdAt", example = "2024-04-11T17:18:53.457879")
    private LocalDateTime createdAt;

    @Schema(name = "likes", example = "1")
    private Integer likes;

    @Schema(name = "region", example = "부산")
    private String region;

    @Schema(name = "images", example = "[ " +
            " \"https://dfbf-footprint.s3.ap-northeast-2.amazonaws.com/472684f1-3618-47ae-8cc4-493179ae85a8_1.jpg\""
            +" ]")
    private List<String> images;

    @Builder
    private ReviewDto(Long memberId, String title,
                      String content, String region,
                      List<String> images, Integer likes) {
        //this.reviewId = reviewId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.region = region;
        this.createdAt = LocalDateTime.now();
    }

    public static ReviewDto of(Review review, List<String> images) {
        return ReviewDto.builder()
                //.reviewId(review.getId())
                .memberId(review.getMember().getId())
                .title(review.getTitle())
                .content(review.getContent())
                .images(images)
                .likes(review.getLikes())
                .build();
    }
}
