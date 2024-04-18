package com.dbfp.footprint.domain.review;

import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.Schedule;
import com.dbfp.footprint.exception.review.NotFoundImageException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "plan_id")
    private Plan plan;

    private String title;

    private String content;

    private Integer likes;

    private String region;

    private boolean visible;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @Builder
    private Review(String title, String content,
                  Member member, boolean visible,
                   String region, Plan plan){
        this.title = title;
        this.content = content;
        this.member = member;
        this.plan = plan;
        this.region = region;
        this.visible = visible;
        this.createdAt = LocalDateTime.now();
        this.likes = 0;
    }

    public static Review of(CreateReviewRequest reviewReqDto, Member member, Plan plan){
        return Review.builder()
                .title(reviewReqDto.getTitle())
                .content(reviewReqDto.getContent())
                .member(member)
                .plan(plan)
                .visible(reviewReqDto.isVisible())
                .region(reviewReqDto.getRegion())
                .build();
    }

    public void update(UpdateReviewRequest reviewReqDto, List<Image> images) {
        this.title = reviewReqDto.getTitle();
        this.content = reviewReqDto.getContent();
        this.visible = reviewReqDto.isVisible();
        this.region = reviewReqDto.getRegion();
        this.images = images;
        this.createdAt = LocalDateTime.now();
    }

    public void addLikes(Member member) {
        ReviewLike reviewLike = new ReviewLike(member, this);
        reviewLikes.add(reviewLike);
        likes += 1;
    }

    public void subLikes(Member member) {
        ReviewLike reviewLike = findReviewLike(member);
        reviewLikes.remove(reviewLike);
        if (likes > 0) {
            likes -= 1;
        }
    }

    private ReviewLike findReviewLike(Member member) {
        return reviewLikes.stream()
                .filter(reviewLike -> reviewLike.getMember().equals(member))
                .findFirst()
                .orElse(null);
    }

    public void addImage(Image image){
        this.images.add(image);
    }
}
