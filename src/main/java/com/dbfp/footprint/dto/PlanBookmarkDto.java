package com.dbfp.footprint.dto;


import com.dbfp.footprint.domain.plan.PlanBookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanBookmarkDto {
    private Long id;
    private Long memberId;
    private Long planId;

    public static PlanBookmarkDto from(PlanBookmark bookmark) {
        PlanBookmarkDto dto = new PlanBookmarkDto();
        dto.setId(bookmark.getId());
        dto.setMemberId(bookmark.getMember().getId());
        dto.setPlanId(bookmark.getPlan().getId());
        return dto;
    }
}
