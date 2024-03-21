package com.dbfp.footprint.dto.review;

import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {
    private Long reviewId;

    private Long memberId;

    private String title;

    private String content;
    //좋아요개수 추가해야함

    @Builder
    private ReviewDto(Long reviewId, Long memberId,
                      String title, String content) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }

    public static ReviewDto of(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .memberId(review.getMember().getId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }
}
