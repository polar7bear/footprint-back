package com.dbfp.footprint.api.service.review;

import com.dbfp.footprint.api.repository.review.ImageRepository;
import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.review.ReviewLikeRepository;
import com.dbfp.footprint.api.repository.review.ReviewRepository;
import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.review.Image;
import com.dbfp.footprint.domain.review.Review;
import com.dbfp.footprint.domain.review.ReviewLike;
import com.dbfp.footprint.dto.review.ReviewDto;
import com.dbfp.footprint.api.request.review.ReviewLikeRequest;
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
    private final ReviewLikeRepository reviewLikeRepository;

    public ReviewService(MemberRepository memberRepository, ImageRepository imageRepository,
                         ReviewRepository reviewRepository, ReviewLikeRepository reviewLikeRepository){
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
        this.reviewRepository = reviewRepository;
        this.reviewLikeRepository = reviewLikeRepository;
    }

    //리뷰 작성
    @Transactional
    public Long create(CreateReviewRequest reviewReqDto){
        Member member = memberRepository.findById(reviewReqDto.getMemberId()).orElseThrow(NotFoundMemberException::new);

        Review review = Review.of(reviewReqDto, member);

        for (Long imageId : reviewReqDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
            image.setReview(review);
            review.addImage(image);
        }
        System.out.println("++++++++리뷰안에 이미지 없는가++++++++++++"+review.getImages().size());
        reviewRepository.save(review);
        return review.getId();
    }

    //리뷰 상세 조회
    @Transactional
    public ReviewDto findById(Long reviewId){
        Review review = reviewRepository.findByIdAndVisible(reviewId, true);
        List<String> images = new ArrayList<>();
        System.out.println("||||||||리뷰에이미지있는가||||||||||||||||||"+review.getImages().size());
        for (Image image : review.getImages()) {
            System.out.println("----------------------이미지ID---------------------------"+image.getId());
            System.out.println("----------------------이미지URL---------------------------"+image.getImageUrl());
            String imageUrl = image.getImageUrl();
            images.add(imageUrl);
        }

        return ReviewDto.of(review, images);
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
        if (sort.equals("id")){
            reviewsListPage = reviewRepository.findAllByVisible(true,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Id")));
        }else if (sort.equals("likes")){
            reviewsListPage = reviewRepository.findAllByVisible(true,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes")));
        }else{
            reviewsListPage = reviewRepository.findAllByVisible(true,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Id")));
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
                    review.getTitle(),
                    review.getCreatedAt(),
                    review.getLikes(),
                    null
            );
        } else {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
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
                    review.getTitle(),
                    review.getCreatedAt(),
                    review.getLikes(),
                    null
            );
        } else {
            return new ReviewListDto(
                    review.getId(),
                    review.getMember().getId(),
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
