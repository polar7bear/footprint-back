package com.dbfp.footprint.api.controller.review;

import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.api.service.review.ReviewService;
import com.dbfp.footprint.dto.review.ReviewDto;
import com.dbfp.footprint.dto.review.ReviewListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
public class ReivewController {
    private final ReviewService reviewService;

    public ReivewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    //리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<Long> createReviews(@RequestBody CreateReviewRequest reviewReqDto){
        Long id = reviewService.create(reviewReqDto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    //리뷰 상세조회
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ReviewDto> findReview(@PathVariable(name = "reviewId") Long reviewId){
        ReviewDto reviewDto = reviewService.findById(reviewId);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    //내가 쓴 리뷰 조회
    @GetMapping("/my/reviews")
    public ResponseEntity<Page<ReviewListDto>> findMyReviews(@RequestParam(value = "memberId") Long memberId,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "8") int size){
        Page<ReviewListDto> myReviews = reviewService.findAllMyReviewPage(memberId, page, size);
        return new ResponseEntity<>(myReviews, HttpStatus.OK);
    }

    //리뷰 전체 조회와 정렬
    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewListDto>> findReviews(@RequestParam(value = "sort", defaultValue = "id") String sort,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "8") int size){
        Page<ReviewListDto> reviews = reviewService.findAllReviewsBySort(sort, page, size);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    //리뷰 수정
    @PutMapping("/reviews")
    public ResponseEntity<String> updateReview(@RequestBody UpdateReviewRequest reviewReqDto){
        reviewService.update(reviewReqDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable(name = "reviewId") Long reviewId){
        reviewService.delete(reviewId);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
