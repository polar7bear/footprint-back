package com.dbfp.footprint.api.controller.member;


import com.dbfp.footprint.api.request.CreateRefreshTokenRequest;
import com.dbfp.footprint.api.request.LoginMemberRequest;
import com.dbfp.footprint.api.request.member.CreateMemberRequest;
import com.dbfp.footprint.api.request.member.DeleteMemberRequest;
import com.dbfp.footprint.api.response.CreateMemberResponse;
import com.dbfp.footprint.api.response.LoginMemberResponse;
import com.dbfp.footprint.api.service.member.MemberService;
import com.dbfp.footprint.config.CustomUserDetails;
import com.dbfp.footprint.config.jwt.JwtFilter;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.util.validation.EmailGroup;
import com.dbfp.footprint.util.validation.PasswordGroup;
import com.dbfp.footprint.util.validation.RequestValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;
    private final RequestValidator requestValidator;

    @PostMapping("/signup")
    public ResponseEntity<CreateMemberResponse> signUp(@Valid @RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = memberService.signUp(request.getEmail(), request.getPassword(), request.getNickname(), request.getKakaoId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> login(@Valid @RequestBody LoginMemberRequest request) {
        LoginMemberResponse response = memberService.login(request.getEmail(), request.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + response.getAccessToken());

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginMemberResponse> refresh(@RequestBody CreateRefreshTokenRequest request) {
        LoginMemberResponse response = memberService.refresh(request);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody CreateRefreshTokenRequest request) {
        boolean deleted = memberService.deleteByToken(request.getRefreshToken());

        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token -> 존재하지 않는 토큰이거나 만료된 토큰입니다.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody DeleteMemberRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<Member> findMember = memberService.findMemberById(userDetails.getId());
        Member member = findMember.get();

        if (member.getKakaoId() != null && !member.getKakaoId().isEmpty()) {
            requestValidator.validate(request, EmailGroup.class);  // EmailGroup 검증 수행
            memberService.deleteMemberByKakaoId(request.getKakaoId(), userDetails.getId());

            return ResponseEntity.ok("회원 탈퇴가 처리되었습니다.");

        } else if (member.getKakaoId() == null) {
            requestValidator.validate(request, PasswordGroup.class);  // PasswordGroup 검증 수행
            memberService.deleteMemberByPassword(request.getPassword(), userDetails.getId());

            return ResponseEntity.ok("회원 탈퇴가 처리되었습니다.");

        } else {
            return ResponseEntity.badRequest().body("필수 입력 사항이 누락되었습니다.");
        }
    }
}
