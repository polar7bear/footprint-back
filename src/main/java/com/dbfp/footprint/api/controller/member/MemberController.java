package com.dbfp.footprint.api.controller.member;


import com.dbfp.footprint.api.request.member.CreateMemberRequest;
import com.dbfp.footprint.api.request.CreateRefreshTokenRequest;
import com.dbfp.footprint.api.request.LoginMemberRequest;
import com.dbfp.footprint.api.response.CreateMemberResponse;
import com.dbfp.footprint.api.response.LoginMemberResponse;
import com.dbfp.footprint.api.service.member.MemberService;
import com.dbfp.footprint.config.jwt.JwtFilter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<CreateMemberResponse> signUp(@Valid @RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = memberService.signUp(request.getEmail(), request.getPassword(), request.getNickname());
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
}
