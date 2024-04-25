package com.dbfp.footprint.api.repository.review;

import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.review.Review;
import com.dbfp.footprint.domain.review.ReviewLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    List<ReviewLike> findByReview(Review review);

    Page<ReviewLike> findAllByMember(Member member, PageRequest pageRequest);

    ReviewLike findByMemberAndReview(Member member, Review review);

    boolean existsByMemberAndReview(Member member, Review review);

    void deleteByMemberId(Long memberId);
}
