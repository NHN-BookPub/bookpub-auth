package com.nhnacademy.bookpubauth.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 프론트 서버에서 전달받은 로그인 인증을 위한 request dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class LoginMemberRequestDto {
    private String id;
    private String pwd;
}
