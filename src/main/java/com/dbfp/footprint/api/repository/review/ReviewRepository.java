package com.dbfp.footprint.api.repository.review;

import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByMember(Member member, PageRequest pageRequest);
}
