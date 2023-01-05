package com.nhnacademy.bookpubauth.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 시 토큰값 발행 controller.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@RestController
public class MemberController {

    @PostMapping("/auth/token")
    public ResponseEntity<String> sendToken() {
        return new ResponseEntity<>(
                "token-21312454129512",
                new HttpHeaders(),
                HttpStatus.OK
        );
    }
}
