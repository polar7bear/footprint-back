package com.dbfp.footprint.api.request.review;

import com.dbfp.footprint.domain.review.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewRequest {
    @Schema(name = "작성자 ID", example = "1")
    private Long memberId;

    @Schema(name = "리뷰 제목", example = "부산 여행 후기")
    private String title;

    @Schema(name = "리뷰 내용", example = "코레일 내일로를 이용한 부산 1박2일 여행")
    private String content;

    @Schema(name = "이미지 ID 배열", example = "[ " +
            "1"
            +" ]")
    private List<Long> imageIds;

    public CreateReviewRequest(Long memberId, String title,
                               String content, List<Long> imageIds){
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imageIds = imageIds;
    }
}
