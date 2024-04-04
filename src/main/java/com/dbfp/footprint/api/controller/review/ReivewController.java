package com.dbfp.footprint.api.controller.review;

import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.request.review.UpdateReviewRequest;
import com.dbfp.footprint.api.service.review.ReviewService;
import com.dbfp.footprint.dto.review.ReviewDto;
import com.dbfp.footprint.dto.review.ReviewLikeDto;
import com.dbfp.footprint.dto.review.ReviewListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Review", description = "Review API")
public class ReivewController {
    private final ReviewService reviewService;

    public ReivewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    //리뷰 작성
    @PostMapping("/reviews")
    @Operation(summary = "리뷰 생성", description = "리뷰 작성 후 저장할 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 생성되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<Long> createReviews(@RequestBody CreateReviewRequest reviewReqDto){
        Long id = reviewService.create(reviewReqDto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    //리뷰 상세조회
    @GetMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 상세조회", description = "하나의 리뷰에 대해 상세 정보를 조회할 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<ReviewDto> findReview(@PathVariable(name = "reviewId") Long reviewId){
        ReviewDto reviewDto = reviewService.findById(reviewId);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    //내가 쓴 리뷰목록 조회
    @GetMapping("/my/reviews")
    @Operation(summary = "내가 쓴 리뷰 목록 조회", description = "내가 작성한 리뷰를 목록으로 받아올 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<Page<ReviewListDto>> findMyReviews(@RequestParam(value = "memberId") Long memberId,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "8") int size){
        Page<ReviewListDto> myReviews = reviewService.findAllMyReviewPage(memberId, page, size);
        return new ResponseEntity<>(myReviews, HttpStatus.OK);
    }

    //내가 좋아요한 리뷰목록 조회
    @GetMapping("/my/liked-reviews")
    @Operation(summary = "내가 좋아요한 리뷰 목록 조회", description = "내가 좋아요 누른 리뷰를 목록으로 받아올 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<Page<ReviewListDto>> findMyLikedReviews(@RequestParam(value = "memberId") Long memberId,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "8") int size){
        Page<ReviewListDto> myReviews = reviewService.findAllMyLikedReviewPage(memberId, page, size);
        return new ResponseEntity<>(myReviews, HttpStatus.OK);
    }

    //리뷰 전체 조회와 정렬
    @GetMapping("/reviews")
    @Operation(summary = "리뷰 전체 조회", description = "메인 화면에서 최신/좋아요 순으로 정렬된 리뷰 목록을 표시할 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<Page<ReviewListDto>> findReviews(@RequestParam(value = "sort", defaultValue = "id") String sort,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "8") int size){
        Page<ReviewListDto> reviews = reviewService.findAllReviewsBySort(sort, page, size);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @Operation(
            summary = "리뷰 검색 결과", description = "제목과 글에 검색어가 포함된 리뷰 목록이 반환되는 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 검색되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 값")
            })
    @GetMapping("/search-reviews")
    public ResponseEntity<Page<ReviewListDto>> searchReviews(@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ReviewListDto> searchReviewList;
        if (searchKeyword == null) {
            searchReviewList = reviewService.findAllReviewsBySort("id", page, size);
        } else {
            searchReviewList = reviewService.searchReviews(searchKeyword, page, size);
        }

        return new ResponseEntity<>(searchReviewList, HttpStatus.OK);
    }

    //좋아요 추가
    @PostMapping("/reviews/add-likes")
    @Operation(summary = "리뷰 좋아요 추가", description = "리뷰에 좋아요를 추가할 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 좋아요가 추가되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "500", description = "좋아요가 이미 눌려있습니다")
    })
    public ResponseEntity<String> addLikes(@RequestBody ReviewLikeDto reviewLikeDto) {
        reviewService.addLikes(reviewLikeDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //좋아요 취소
    @PostMapping("/reviews/sub-likes")
    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰에 좋아요 취소할 때 쓰는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 좋아요가 취소되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "500", description = "좋아요가 이미 취소되었습니다")
    })
    public ResponseEntity<String> subLikes(@RequestBody ReviewLikeDto likeCommentDto) {
        reviewService.subLikes(likeCommentDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //리뷰 수정
    @PutMapping("/reviews")
    @Operation(summary = "리뷰 수정", description = "리뷰 작성자가 본인의 리뷰를 수정할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 수정되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<String> updateReview(@RequestBody UpdateReviewRequest reviewReqDto){
        reviewService.update(reviewReqDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰 작성자가 본인의 리뷰를 삭제할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 삭제되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<String> deleteReview(@PathVariable(name = "reviewId") Long reviewId){
        reviewService.delete(reviewId);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
