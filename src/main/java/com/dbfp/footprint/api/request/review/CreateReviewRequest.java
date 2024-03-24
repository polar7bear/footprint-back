package com.dbfp.footprint.api.request.review;

import com.dbfp.footprint.domain.review.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewRequest {
    private Long memberId;

    private String title;

    private String content;

    private List<Long> imageIds;

    public CreateReviewRequest(Long memberId, String title,
                               String content, List<Long> imageIds){
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imageIds = imageIds;
    }
}
