package com.dbfp.footprint.dto;


import com.dbfp.footprint.domain.plan.PlanBookmark;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "계획 북마크 정보")
public class PlanBookmarkDto {
    @Schema(description = "북마크 ID", example = "123")
    private Long id;
    @Schema(description = "회원 ID", example = "456")
    private Long memberId;
    @Schema(description = "계획 ID", example = "789")
    private Long planId;

    public static PlanBookmarkDto from(PlanBookmark bookmark) {
        PlanBookmarkDto dto = new PlanBookmarkDto();
        dto.setId(bookmark.getId());
        dto.setMemberId(bookmark.getMember().getId());
        dto.setPlanId(bookmark.getPlan().getId());
        return dto;
    }
}
