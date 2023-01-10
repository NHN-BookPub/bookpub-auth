package com.nhnacademy.bookpubauth.member.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * member 인증 후 토큰 발급을 위한 DTO.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class LoginMemberRequestDto {
    private String userId;
    private List<String> authorities;
}
