package com.dbfp.footprint.api.repository.review;

import com.dbfp.footprint.domain.review.Image;
import com.dbfp.footprint.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageByConvertImageNameContaining(String fileName);
    void deleteImagesByReview(Review review);
}
