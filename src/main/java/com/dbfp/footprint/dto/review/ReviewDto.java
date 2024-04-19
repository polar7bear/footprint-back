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
    @Schema(name = "memberId", example = "1", required = true)
    private Long memberId;

    @Schema(name = "planId", example = "1")
    private Long planId;

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

    @Schema(name = "visible", example = "true")
    private boolean visible;

    @Schema(name = "images", example = "[ " +
            " \"https://dfbf-footprint.s3.ap-northeast-2.amazonaws.com/472684f1-3618-47ae-8cc4-493179ae85a8_1.jpg\""
            +" ]")
    private List<String> images;

    @Schema(name = "imageIds", example = "[ " +
            "1"
            +" ]")
    private List<Long> imageIds;

    @Builder
    private ReviewDto(Long memberId, String title,
                      String content, String region,
                      List<String> images, Integer likes,
                      Long planId, LocalDateTime createdAt,
                      List<Long> imageIds, boolean visible) {
        this.memberId = memberId;
        this.planId = planId;
        this.title = title;
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.region = region;
        this.visible = visible;
        this.createdAt = createdAt;
        this.imageIds = imageIds;
    }

    public static ReviewDto of(Review review, List<String> images, List<Long> imageIds) {
        if(review.getPlan()==null){
            return ReviewDto.builder()
                    .memberId(review.getMember().getId())
                    .planId(null)
                    .region(review.getRegion())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .images(images)
                    .likes(review.getLikes())
                    .createdAt(review.getCreatedAt())
                    .imageIds(imageIds)
                    .visible(review.isVisible())
                    .build();
        }else {
            return ReviewDto.builder()
                    .memberId(review.getMember().getId())
                    .planId(review.getPlan().getId())
                    .region(review.getRegion())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .images(images)
                    .imageIds(imageIds)
                    .visible(review.isVisible())
                    .likes(review.getLikes())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }
}
