package com.dbfp.footprint.api.service.review;

import com.dbfp.footprint.api.repository.review.ImageRepository;
import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.review.ReviewRepository;
import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.review.Image;
import com.dbfp.footprint.domain.review.Review;
import com.dbfp.footprint.dto.review.ReviewDto;
import com.dbfp.footprint.dto.review.ReviewListDto;
import com.dbfp.footprint.exception.member.NotFoundMemberException;
import com.dbfp.footprint.exception.review.NotFoundImageException;
import com.dbfp.footprint.exception.review.NotFoundReviewException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(MemberRepository memberRepository, ImageRepository imageRepository, ReviewRepository reviewRepository){
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
        this.reviewRepository = reviewRepository;
    }

    //리뷰 작성
    public Long create(CreateReviewRequest reviewReqDto){
        Member member = memberRepository.findById(reviewReqDto.getMemberId()).orElseThrow(NotFoundMemberException::new);
        List<Image> images = new ArrayList<>();
        for (Long imageId : reviewReqDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
            images.add(image);
        }

        Review review = Review.of(reviewReqDto, member, images);
        reviewRepository.save(review);
        return review.getId();
    }

    //리뷰 상세 조회
    public ReviewDto findById(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundReviewException::new);
        List<Long> images = new ArrayList<>();
        for (Image image : review.getImages()) {
            Long imageId = image.getId();
            images.add(imageId);
        }

        return ReviewDto.of(review, images);
    }

    @Transactional
    public Page<ReviewListDto> findAllMyReviewPage(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<Review> reviewsListPage = reviewRepository.findAllByMember(member,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Id")));

        return reviewsListPage.map(this::reviewListMap);
    }

    private ReviewListDto reviewListMap(Review review) {
        if (review.getImages().isEmpty()) {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
                    review.getTitle(),
                    null
            );
        } else {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
                    review.getTitle(),
                    review.getImages().get(0).getImageUrl()
            );
        }
    }

    // 리뷰 수정
    @Transactional
    public void update(UpdateReviewRequest reviewReqDto) {
        List<Image> images = new ArrayList<>();
        for (Long imageId : reviewReqDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
            images.add(image);
        }
        reviewRepository.findById(reviewReqDto.getReviewId()).orElseThrow(NotFoundReviewException::new).update(reviewReqDto, images);
    }

    //리뷰삭제 (회원본인인지 검사 필요 memberid를 받든, athor토큰으로 회원확인을 하든)
    @Transactional
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundReviewException::new);

        imageRepository.deleteImagesByReview(review);
        reviewRepository.delete(review);
    }
}
