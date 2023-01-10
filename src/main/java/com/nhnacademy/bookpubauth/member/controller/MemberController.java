package com.nhnacademy.bookpubauth.member.controller;

import com.nhnacademy.bookpubauth.member.dto.LoginMemberRequestDto;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberResponseDto;
import com.nhnacademy.bookpubauth.member.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * jwt 발급을 위한 컨트롤러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/token")
public class MemberController {
    private final TokenService tokenService;

    @GetMapping("/issue")
    public ResponseEntity<LoginMemberResponseDto> tokenIssued(@RequestBody LoginMemberRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tokenService.tokenIssued(requestDto.getUserId(), requestDto.getAuthorities()));
    }

    @GetMapping("/reissue")
    public ResponseEntity<LoginMemberResponseDto> tokenReIssued(@RequestBody LoginMemberRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tokenService.tokenReIssued(requestDto.getUserId(), requestDto.getAuthorities()));
    }
}
