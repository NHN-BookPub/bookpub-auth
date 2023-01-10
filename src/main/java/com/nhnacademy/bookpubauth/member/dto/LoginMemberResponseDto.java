package com.nhnacademy.bookpubauth.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 반환 DTO 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Getter
@AllArgsConstructor
public class LoginMemberResponseDto {
    private String token;
}
