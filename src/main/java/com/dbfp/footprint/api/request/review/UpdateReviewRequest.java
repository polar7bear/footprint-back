package com.dbfp.footprint.api.request.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateReviewRequest {
    @Schema(name = "reviewId", example = "1")
    private Long reviewId;

    @Schema(name = "memberId", example = "1")
    private Long memberId;

    @Schema(name = "title", example = "부산 여행")
    private String title;

    @Schema(name = "content", example = "코레일 내일로를 이용한 부산 1박2일 여행")
    private String content;

    @Schema(name = "imageIds", example = "[ " +
            "1"
            +" ]")
    private List<Long> imageIds;

    public UpdateReviewRequest(Long memberId, String title,
                               String content, List<Long> imageIds){
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imageIds = imageIds;
    }
}
