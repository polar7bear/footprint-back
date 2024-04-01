package com.dbfp.footprint.domain.plan;

import com.dbfp.footprint.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "plan_bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanBookmark {

    @Id
    @Column(name = "plan_bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "plan_id")
    private Plan plan;

    public static PlanBookmark of(Member member, Plan plan) {
        PlanBookmark bookmark = new PlanBookmark();
        bookmark.member = member;
        bookmark.plan = plan;
        return bookmark;
    }
}



