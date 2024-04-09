package com.dbfp.footprint.api.controller.member;

import com.dbfp.footprint.api.request.member.CreateMemberRequest;
import com.dbfp.footprint.api.request.review.CreateReviewRequest;
import com.dbfp.footprint.api.service.member.MemberService;
import com.dbfp.footprint.api.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }


    //임시 멤버 생성
    @PostMapping("/member")
    @Operation(summary = "임시 멤버 생성 API", description = "원래는 회원가입을 통해 생성됩니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버가 성공적으로 생성되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    public ResponseEntity<Long> createMember(@RequestBody CreateMemberRequest createMemberRequest){
        Long id = memberService.createMember(createMemberRequest);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
