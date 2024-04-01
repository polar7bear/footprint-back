package com.dbfp.footprint.dto.review;

import com.dbfp.footprint.domain.review.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {
    //private Long reviewId;

    @Schema(name = "작성자 멤버ID", example = "1", required = true)
    private Long memberId;

    @Schema(name = "리뷰 제목", example = "1박 2일 부산여행")
    private String title;

    @Schema(name = "리뷰 내용", example = "1")
    private String content;

    @Schema(name = "이미지 URL 배열", example = "[ " +
            " \"https://dfbf-footprint.s3.ap-northeast-2.amazonaws.com/472684f1-3618-47ae-8cc4-493179ae85a8_1.jpg\""
            +" ]")
    private List<String> images;

    //좋아요 개수 추가해야함
    @Schema(name = "좋아요 개수", example = "1")
    private Integer likes;

    @Builder
    private ReviewDto(Long memberId,
                      String title, String content,
                      List<String> images, Integer likes) {
        //this.reviewId = reviewId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.images = images;
        this.likes = likes;
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
