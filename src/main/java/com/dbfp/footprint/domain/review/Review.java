package com.dbfp.footprint.domain.review;

import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.exception.review.NotFoundImageException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    private String title;

    private String content;

    @OneToMany(mappedBy = "review")
    private List<Image> images;

    @Builder
    private Review(String title, String content,
                  Member member, List<Image> images) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.images = images;
    }

    public static Review of(CreateReviewRequest reviewReqDto, Member member, List<Image> images) {
        return Review.builder()
                .title(reviewReqDto.getTitle())
                .content(reviewReqDto.getContent())
                .member(member)
                .images(images)
                .build();
    }

    public void update(UpdateReviewRequest reviewReqDto, List<Image> images) {
        this.title = reviewReqDto.getTitle();
        this.content = reviewReqDto.getContent();
        this.images = images;
    }
}
