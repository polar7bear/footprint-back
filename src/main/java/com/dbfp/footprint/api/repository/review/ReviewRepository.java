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

    // 리뷰 검색기능 (findBy(컬럼이름)Containing -> 컬럼에서 키워드가 포함된 것을 찾겠다. 글자 한 글자만 검색해도 그 글자가 포함된 것은 다 나옴)
    Page<Review> findByTitleContainingOrContentContaining(String searchKeyword1, String searchKeyword2, PageRequest pageRequest);
}
