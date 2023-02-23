package com.nhnacademy.bookpubauth.token.controller;

import static com.nhnacademy.bookpubauth.token.util.JwtUtil.ACCESS_TOKEN_VALID_TIME;
import static com.nhnacademy.bookpubauth.token.util.JwtUtil.AUTH_HEADER;
import static com.nhnacademy.bookpubauth.token.util.JwtUtil.EXP_HEADER;

import com.nhnacademy.bookpubauth.token.service.TokenService;
import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private static final String EXP_MESSAGE = "다시 로그인 하세요.";
    private static final String TOKEN_INVALID_MESSAGE = "유효하지 않은 토큰입니다.";

    /**
     * 재발급 요청을 받은 후 토큰을 갱신해주는 메소드.
     *
     * @param accessToken 재발급 요청을 한 accessToken.
     * @return 재발급 된 accessToken.
     */
    @PostMapping("/reissue")
    public ResponseEntity<Void> tokenReIssued(@RequestBody String accessToken) {
        String result = tokenService.tokenReIssued(accessToken);

        if (result.equals(EXP_MESSAGE) || result.equals(TOKEN_INVALID_MESSAGE)) {
            return ResponseEntity.ok()
                    .header("X-MESSAGE", result)
                    .build();
        }

        return ResponseEntity.ok()
                .header(JwtUtil.AUTH_HEADER, JwtUtil.TOKEN_TYPE + result)
                .header(EXP_HEADER, String.valueOf(new Date().getTime() + ACCESS_TOKEN_VALID_TIME))
                .build();
    }

    /**
     * 회원 로그아웃 api.
     *
     * @param request 요청
     * @return ok.
     */
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String jwt = request.getHeader(AUTH_HEADER);
        tokenService.logout(jwt);

        return ResponseEntity.ok()
                .build();
    }
}
