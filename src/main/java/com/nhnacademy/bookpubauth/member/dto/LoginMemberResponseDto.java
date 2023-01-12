package com.nhnacademy.bookpubauth.member.dto;

import java.util.List;
import lombok.Getter;

/**
 * 토큰 반환 DTO 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Getter
public class LoginMemberResponseDto {
    private String memberId;
    private String memberPwd;
    private List<String> authorities;
}
