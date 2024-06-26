package com.dbfp.footprint.api.service.review;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.repository.review.ImageRepository;
import com.dbfp.footprint.api.repository.review.ReviewLikeRepository;
import com.dbfp.footprint.api.repository.review.ReviewRepository;
import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.ReviewLikeRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.review.Image;
import com.dbfp.footprint.domain.review.Review;
import com.dbfp.footprint.domain.review.ReviewLike;
import com.dbfp.footprint.dto.review.ReviewDto;
import com.dbfp.footprint.dto.review.ReviewListDto;
import com.dbfp.footprint.exception.member.NotFoundMemberException;
import com.dbfp.footprint.exception.plan.PlanNotFoundException;
import com.dbfp.footprint.exception.review.NotFoundImageException;
import com.dbfp.footprint.exception.review.NotFoundReviewException;
import com.dbfp.footprint.exception.review.UnauthorizedAccessException;
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
    private final ReviewLikeRepository reviewLikeRepository;
    private final PlanRepository planRepository;

    public ReviewService(MemberRepository memberRepository, ImageRepository imageRepository,
                         ReviewRepository reviewRepository, ReviewLikeRepository reviewLikeRepository,
                         PlanRepository planRepository) {
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
        this.imageRepository = imageRepository;
        this.reviewRepository = reviewRepository;
        this.reviewLikeRepository = reviewLikeRepository;
    }

    //리뷰 작성
    @Transactional
    public Long create(CreateReviewRequest reviewReqDto) {
        Member member = memberRepository.findById(reviewReqDto.getMemberId()).orElseThrow(NotFoundMemberException::new);
        Plan plan = null;
        if (reviewReqDto.getPlanId() != null) {
            plan = planRepository.findById(reviewReqDto.getPlanId()).orElseThrow(PlanNotFoundException::new);
        }
        Review review = Review.of(reviewReqDto, member, plan);

        for (Long imageId : reviewReqDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
            image.setReview(review);
            review.addImage(image);
        }
        reviewRepository.save(review);
        return review.getId();
    }

    //리뷰 상세 조회
    @Transactional
    public ReviewDto findById(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundReviewException::new);

        //리뷰 작성자의 고유번호
        Long reviewOwnerMemberId = review.getMember().getId();

        //공개상태면 그냥 리뷰상세조회 리턴 or 작성자가 일치하다면
        if (review.isVisible() || reviewOwnerMemberId.equals(memberId)) {
            List<String> images = new ArrayList<>();
            List<Long> imageIds = new ArrayList<>();

            for (Image image : review.getImages()) {
                String imageUrl = image.getImageUrl();
                Long imageId = image.getId();

                images.add(imageUrl);
                imageIds.add(imageId);
            }

            return ReviewDto.of(review, images, imageIds);
        } else { // 비공개 리뷰라면 & 작성자가 일치하지 않다면
            throw new UnauthorizedAccessException();
        }
    }

    //내가 작성한 리뷰 목록 조회
    @Transactional
    public Page<ReviewListDto> findAllMyReviewPage(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<Review> reviewsListPage = reviewRepository.findAllByMember(member,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Id")));

        return reviewsListPage.map(this::reviewListMap);
    }

    //내가 좋아요 누른 리뷰 목록 조회
    @Transactional
    public Page<ReviewListDto> findAllMyLikedReviewPage(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<ReviewLike> reviewLikesListPage = reviewLikeRepository.findAllByMember(member,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Id")));

        return reviewLikesListPage.map(this::reviewLikeMap);
    }

    //정렬 및 전체조회
    @Transactional
    public Page<ReviewListDto> findAllReviewsBySort(String sort, int page, int size) {
        Page<Review> reviewsListPage;
        if (sort.equals("id")) {
            reviewsListPage = reviewRepository.findAllByVisible(true,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "Id")));
        } else if (sort.equals("likes")) {
            reviewsListPage = reviewRepository.findAllByVisible(true,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes")));
        } else {
            reviewsListPage = reviewRepository.findAllByVisible(true,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "Id")));
        }


        return reviewsListPage.map(this::reviewListMap);
    }

    //리뷰 검색
    @Transactional
    public Page<ReviewListDto> searchReviews(String searchKeyword, int page, int size) {
        Page<Review> noticeSearchPage = reviewRepository.findByVisibleAndTitleContainingOrContentContaining(true,
                searchKeyword.trim(), searchKeyword.trim(), PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Id")));

        return noticeSearchPage.map(this::reviewListMap);
    }

    private ReviewListDto reviewListMap(Review review) {
        if (review.getImages().isEmpty()) {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
                    review.getMember().getNickname(), //
                    review.getTitle(),
                    review.getCreatedAt(),
                    review.getLikes(),
                    null
            );
        } else {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
                    review.getMember().getNickname(),
                    review.getTitle(),
                    review.getCreatedAt(),
                    review.getLikes(),
                    review.getImages().get(0).getImageUrl()
            );
        }
    }

    private ReviewListDto reviewLikeMap(ReviewLike reviewLike) {
        Review review = reviewLike.getReview();
        if (review.getImages().isEmpty()) {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
                    review.getMember().getNickname(),
                    review.getTitle(),
                    review.getCreatedAt(),
                    review.getLikes(),
                    null
            );
        } else {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
                    review.getMember().getNickname(),
                    review.getTitle(),
                    review.getCreatedAt(),
                    review.getLikes(),
                    review.getImages().get(0).getImageUrl()
            );
        }
    }

    //리뷰 좋아요 추가
    @Transactional
    public void addLikes(ReviewLikeRequest reviewLikeRequest) {
        Member member = memberRepository.findById(reviewLikeRequest.getMemberId())
                .orElseThrow(NotFoundMemberException::new);
        Review review = reviewRepository.findById(reviewLikeRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);

        if (reviewLikeRepository.existsByMemberAndReview(member, review)) {
            throw new IllegalArgumentException();
        }

        review.addLikes(member);
        reviewRepository.save(review);
    }

    //리뷰 좋아요 취소
    @Transactional
    public void subLikes(ReviewLikeRequest reviewLikeRequest) {
        Member member = memberRepository.findById(reviewLikeRequest.getMemberId())
                .orElseThrow(NotFoundMemberException::new);
        Review review = reviewRepository.findById(reviewLikeRequest.getReviewId()).orElseThrow(NotFoundReviewException::new);

        if (!reviewLikeRepository.existsByMemberAndReview(member, review)) {
            throw new IllegalArgumentException();
        }

        review.subLikes(member);
        reviewRepository.save(review);
    }

    // 리뷰 수정
    @Transactional
    public void update(UpdateReviewRequest reviewReqDto) {
        Review review = reviewRepository.findById(reviewReqDto.getReviewId()).orElseThrow(NotFoundReviewException::new);
        review.getImages().clear();

        for (Long imageId : reviewReqDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
            image.setReview(review);
            review.addImage(image);
        }

        review.update(reviewReqDto);
    }

    //리뷰삭제 (회원본인인지 검사 필요 memberid를 받든, athor토큰으로 회원확인을 하든)
    @Transactional
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundReviewException::new);

        imageRepository.deleteImagesByReview(review);
        reviewRepository.delete(review);
    }
}
