package com.nhnacademy.bookpubauth.token.controller;

import com.nhnacademy.bookpubauth.member.dto.MemberInfoRequestDto;
import com.nhnacademy.bookpubauth.member.dto.MemberInfoResponseDto;
import com.nhnacademy.bookpubauth.token.service.TokenService;
import lombok.RequiredArgsConstructor;
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
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/reissue")
    public ResponseEntity<MemberInfoResponseDto> tokenReIssued(@RequestBody MemberInfoRequestDto requestDto) {
        return null;
    }
}
