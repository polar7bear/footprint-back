package com.dbfp.footprint.api.request.review;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateReviewRequest {
    private Long reviewId;

    private Long memberId;

    private String title;

    private String content;

    private List<Long> imageIds;

    public UpdateReviewRequest(Long memberId, String title,
                               String content, List<Long> imageIds){
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imageIds = imageIds;
    }
}
