package com.nhnacademy.bookpubauth.token.controller;

import static com.nhnacademy.bookpubauth.token.util.JwtUtil.ACCESS_TOKEN_VALID_TIME;
import static com.nhnacademy.bookpubauth.token.util.JwtUtil.EXP_HEADER;

import com.nhnacademy.bookpubauth.token.service.TokenService;
import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/auth")
@Slf4j
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<Void> tokenReIssued(
            @RequestBody String accessToken) {
        String result = tokenService.tokenReIssued(accessToken);

        if (result.contains("로그인")) {
            return ResponseEntity.ok()
                    .header("X-MESSAGE", result)
                    .build();
        }

        return ResponseEntity.ok()
                .header(JwtUtil.AUTH_HEADER, JwtUtil.TOKEN_TYPE + result)
                .header(EXP_HEADER, String.valueOf(new Date().getTime() + ACCESS_TOKEN_VALID_TIME))
                .build();
    }
}
